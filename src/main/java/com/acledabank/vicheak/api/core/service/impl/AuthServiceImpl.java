package com.acledabank.vicheak.api.core.service.impl;

import com.acledabank.vicheak.api.core.dto.*;
import com.acledabank.vicheak.api.core.entity.User;
import com.acledabank.vicheak.api.core.mail.Mail;
import com.acledabank.vicheak.api.core.mail.MailService;
import com.acledabank.vicheak.api.core.mapper.AuthMapper;
import com.acledabank.vicheak.api.core.repository.UserRepository;
import com.acledabank.vicheak.api.core.service.AuthService;
import com.acledabank.vicheak.api.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final AuthMapper authMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtEncoder jwtEncoder;
    private JwtEncoder jwtRefreshTokenEncoder;

    @Autowired
    public void setJwtRefreshTokenEncoder(@Qualifier("jwtRefreshTokenEncoder") JwtEncoder jwtRefreshTokenEncoder) {
        this.jwtRefreshTokenEncoder = jwtRefreshTokenEncoder;
    }

    @Value("${mail.title}")
    private String mailTitle;
    @Value("${spring.mail.username}")
    private String adminMail;
    private final String scopeKey = "scope";
    private final String issuerKey = "public";
    private final String audienceKey = "Public Client";

    @Override
    public AuthResponseDto login(AuthDto authDto) {
        log.info("username : {}, password : {}", authDto.username(), authDto.password());
        Authentication auth = new UsernamePasswordAuthenticationToken(authDto.username(), authDto.password());
        auth = authenticationProvider.authenticate(auth);
        log.info("auth roles : {}", auth.getAuthorities());
        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        return AuthResponseDto.builder()
                .type("Bearer")
                .accessToken(generateAccessToken(GenerateTokenDto.builder()
                        .auth(auth.getName())
                        .scope(scope)
                        .expiration(Instant.now().plus(1, ChronoUnit.HOURS))
                        .build()))
                .refreshToken(generateRefreshToken(GenerateTokenDto.builder()
                        .auth(auth.getName())
                        .scope(scope)
                        .expiration(Instant.now().plus(30, ChronoUnit.DAYS))
                        .build()))
                .build();
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenDto refreshTokenDto) {
        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenDto.refreshToken());
        auth = jwtAuthenticationProvider.authenticate(auth);
        Jwt jwt = (Jwt) auth.getPrincipal();
        return AuthResponseDto.builder()
                .type("Bearer")
                .accessToken(generateAccessToken(GenerateTokenDto.builder()
                        .auth(jwt.getId())
                        .scope(jwt.getClaimAsString(scopeKey))
                        .expiration(Instant.now().plus(1, ChronoUnit.HOURS))
                        .build()))
                .refreshToken(generateRefreshTokenCheckDuration(GenerateTokenDto.builder()
                        .auth(jwt.getId())
                        .scope(jwt.getClaimAsString(scopeKey))
                        .previousToken(refreshTokenDto.refreshToken())
                        .expiration(Instant.now().plus(30, ChronoUnit.DAYS))
                        .duration(Duration.between(Instant.now(), jwt.getExpiresAt()))
                        .checkDurationNumber(7)
                        .build()))
                .build();
    }

    @Transactional
    @Override
    public void register(RegisterDto registerDto) throws MessagingException {
        //map from registerDto to transactionUserDto
        TransactionUserDto transactionUserDto =
                authMapper.fromRegisterDtoToTransactionUserDto(registerDto);

        //validate the dto
        userService.validateTransactionUserDto(transactionUserDto);

        //map from dto to entity
        User newUser = userService.setupNewUser(transactionUserDto);

        userService.updateUserRolesTransaction(newUser, Set.of(2));

        userRepository.save(newUser);

        updateVerifiedCodeAndSendMail(newUser, mailTitle);
    }

    @Transactional
    @Override
    public void verify(VerifyDto verifyDto) {
        User verifiedUser = userRepository.findByEmailAndVerifiedCodeAndVerifiedFalseAndEnabledFalse(
                        verifyDto.email(), verifyDto.verifiedCode())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Email verification has been failed!")
                );

        //make the user verified
        verifiedUser.setVerified(true);
        verifiedUser.setEnabled(true);
        verifiedUser.setVerifiedCode(null);

        userRepository.save(verifiedUser);
    }

    @Transactional
    @Override
    public PasswordTokenDto forgetPassword(ForgetPasswordDto forgetPasswordDto) throws MessagingException {
        User user = userRepository.findByEmail(forgetPasswordDto.email())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with email, %s has not been found in the system!"
                                        .formatted(forgetPasswordDto.email()))
                );

        String passwordToken = RandomUtil.randomTokenGenerator(30);

        user.setVerified(false);
        user.setEnabled(false);
        user.setPasswordToken(passwordToken);
        userRepository.save(user);

        updateVerifiedCodeAndSendMail(user, mailTitle);

        return PasswordTokenDto.builder()
                .message("Please check your email for verification code and verify your account to reset password!")
                .token(passwordToken)
                .build();
    }

    @Transactional
    @Override
    public void sendVerificationCode(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with email, %s has not been found in the system!"
                                        .formatted(email))
                );

        updateVerifiedCodeAndSendMail(user, mailTitle);
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = userRepository.findByEmailAndVerifiedTrueAndEnabledTrue(resetPasswordDto.email())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Email has been not found or unauthorized to reset password!")
                );

        if (!resetPasswordDto.token().equals(user.getPasswordToken()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Password token is invalid, unauthorized access!");

        if (!resetPasswordDto.password().equals(resetPasswordDto.passwordConfirmation()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Password and password confirmation must be matched!");

        user.setPassword(passwordEncoder.encode(resetPasswordDto.password()));
        user.setPasswordToken(null);
        userRepository.save(user);
    }

    private String generateAccessToken(GenerateTokenDto generateTokenDto) {
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(generateTokenDto.auth())
                .issuer(issuerKey)
                .issuedAt(Instant.now())
                .expiresAt(generateTokenDto.expiration())
                .subject("Access Token")
                .audience(List.of(audienceKey))
                .claim(scopeKey, generateTokenDto.scope())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    private String generateRefreshToken(GenerateTokenDto generateTokenDto) {
        JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                .id(generateTokenDto.auth())
                .issuer(issuerKey)
                .issuedAt(Instant.now())
                .expiresAt(generateTokenDto.expiration())
                .subject("Refresh Token")
                .audience(List.of(audienceKey))
                .claim(scopeKey, generateTokenDto.scope())
                .build();
        return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
    }

    private String generateRefreshTokenCheckDuration(GenerateTokenDto generateTokenDto) {
        if (generateTokenDto.duration().toDays() < generateTokenDto.checkDurationNumber()) {
            JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                    .id(generateTokenDto.auth())
                    .issuer(issuerKey)
                    .issuedAt(Instant.now())
                    .expiresAt(generateTokenDto.expiration())
                    .subject("Refresh Token")
                    .audience(List.of(audienceKey))
                    .claim(scopeKey, generateTokenDto.scope())
                    .build();
            return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
        }
        return generateTokenDto.previousToken();
    }

    private void updateVerifiedCodeAndSendMail(User user, String subject) throws MessagingException {
        //generate six random digit for verification code
        String verifiedCode = RandomUtil.getRandomNumber();

        //update verification code
        userRepository.updateVerifiedCode(user.getEmail(), verifiedCode);

        buildAndSendMail(user.getEmail(), subject, verifiedCode);
    }

    private void buildAndSendMail(String email, String subject, String verifiedCode) throws jakarta.mail.MessagingException {
        Mail<String> verifiedMail = new Mail<>();
        verifiedMail.setSender(adminMail);
        verifiedMail.setReceiver(email);
        verifiedMail.setSubject(subject);
        verifiedMail.setTemplate("auth/verify-mail.html");
        verifiedMail.setMetaData(verifiedCode);

        mailService.sendMail(verifiedMail);
    }

}
