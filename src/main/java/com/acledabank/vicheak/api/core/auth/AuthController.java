package com.acledabank.vicheak.api.core.auth;

import com.acledabank.vicheak.api.core.dto.AuthDto;
import com.acledabank.vicheak.api.core.dto.AuthResponseDto;
import com.acledabank.vicheak.api.core.dto.RefreshTokenDto;
import com.acledabank.vicheak.api.core.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
