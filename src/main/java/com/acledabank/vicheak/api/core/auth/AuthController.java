package com.acledabank.vicheak.api.core.auth;

import com.acledabank.vicheak.api.core.dto.*;
import com.acledabank.vicheak.api.core.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/authenticate")
    public AuthResponseDto login(@RequestBody @Valid AuthDto authDto){
        return authService.login(authDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/refreshToken")
    public AuthResponseDto refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        return authService.refreshToken(refreshTokenDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid RegisterDto registerDto) throws MessagingException {
        authService.register(registerDto);
        return Map.of("message", "Please check your email for verification code!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public Map<String, String> verify(@RequestBody @Valid VerifyDto verifyDto) {
        authService.verify(verifyDto);
        return Map.of("message", "Congratulation! Your email has been verified...!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forget-password")
    public PasswordTokenDto forgetPassword(@RequestBody @Valid ForgetPasswordDto forgetPasswordDto) throws MessagingException {
        return authService.forgetPassword(forgetPasswordDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/send-verification-code")
    public Map<String, String> sendVerificationCode(@RequestBody @Valid VerificationCodeDto verificationCodeDto) throws MessagingException {
        authService.sendVerificationCode(verificationCodeDto.email());
        return Map.of("message", "Please check your email for verification code!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto){
        authService.resetPassword(resetPasswordDto);
        return Map.of("message", "Your password has been reset successfully!");
    }

}
