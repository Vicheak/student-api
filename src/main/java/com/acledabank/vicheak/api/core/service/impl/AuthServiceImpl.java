package com.acledabank.vicheak.api.core.service.impl;

import com.acledabank.vicheak.api.core.dto.AuthDto;
import com.acledabank.vicheak.api.core.dto.AuthResponseDto;
import com.acledabank.vicheak.api.core.dto.GenerateTokenDto;
import com.acledabank.vicheak.api.core.dto.RefreshTokenDto;
import com.acledabank.vicheak.api.core.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtEncoder jwtEncoder;
    private JwtEncoder jwtRefreshTokenEncoder;

    @Autowired
    public void setJwtRefreshTokenEncoder(@Qualifier("jwtRefreshTokenEncoder") JwtEncoder jwtRefreshTokenEncoder) {
        this.jwtRefreshTokenEncoder = jwtRefreshTokenEncoder;
    }

    @Override
    public AuthResponseDto login(AuthDto authDto) {
        log.info("username : {}, password : {}", authDto.username(), authDto.password());
        Authentication auth = new UsernamePasswordAuthenticationToken(authDto.username(), authDto.password());
        auth = authenticationProvider.authenticate(auth);
        return AuthResponseDto.builder()
                .type("Bearer")
                .accessToken(generateAccessToken(GenerateTokenDto.builder()
                        .auth(auth.getName())
                        .scope("admin")
                        .expiration(Instant.now().plus(1, ChronoUnit.MINUTES))
                        .build()))
                .refreshToken(generateRefreshToken(GenerateTokenDto.builder()
                        .auth(auth.getName())
                        .scope("admin")
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
                        .scope(jwt.getClaimAsString("scope"))
                        .expiration(Instant.now().plus(1, ChronoUnit.MINUTES))
                        .build()))
                .refreshToken(generateRefreshTokenCheckDuration(GenerateTokenDto.builder()
                        .auth(jwt.getId())
                        .scope(jwt.getClaimAsString("scope"))
                        .previousToken(refreshTokenDto.refreshToken())
                        .expiration(Instant.now().plus(30, ChronoUnit.DAYS))
                        .duration(Duration.between(Instant.now(), jwt.getExpiresAt()))
                        .checkDurationNumber(7)
                        .build()))
                .build();
    }

    private String generateAccessToken(GenerateTokenDto generateTokenDto) {
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(generateTokenDto.auth())
                .issuer("public")
                .issuedAt(Instant.now())
                .expiresAt(generateTokenDto.expiration())
                .subject("Access Token")
                .audience(List.of("Public Client"))
                .claim("scope", generateTokenDto.scope())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    private String generateRefreshToken(GenerateTokenDto generateTokenDto) {
        JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                .id(generateTokenDto.auth())
                .issuer("public")
                .issuedAt(Instant.now())
                .expiresAt(generateTokenDto.expiration())
                .subject("Refresh Token")
                .audience(List.of("Public Client"))
                .claim("scope", generateTokenDto.scope())
                .build();
        return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
    }

    private String generateRefreshTokenCheckDuration(GenerateTokenDto generateTokenDto) {
        if (generateTokenDto.duration().toDays() < generateTokenDto.checkDurationNumber()) {
            JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                    .id(generateTokenDto.auth())
                    .issuer("public")
                    .issuedAt(Instant.now())
                    .expiresAt(generateTokenDto.expiration())
                    .subject("Refresh Token")
                    .audience(List.of("Public Client"))
                    .claim("scope", generateTokenDto.scope())
                    .build();
            return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
        }
        return generateTokenDto.previousToken();
    }

}
