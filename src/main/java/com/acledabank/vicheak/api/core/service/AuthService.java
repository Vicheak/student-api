package com.acledabank.vicheak.api.core.service;

import com.acledabank.vicheak.api.core.dto.AuthDto;
import com.acledabank.vicheak.api.core.dto.AuthResponseDto;
import com.acledabank.vicheak.api.core.dto.RefreshTokenDto;

public interface AuthService {

    /**
     * This method is used to authenticate from client
     * @param authDto is the request from client
     * @return AuthResponseDto
     */
    AuthResponseDto login(AuthDto authDto);

    /**
     * This method is used to generate new access token after expiration
     * @param refreshTokenDto is the request from client
     * @return AuthDto
     */
    AuthResponseDto refreshToken(RefreshTokenDto refreshTokenDto);

}
