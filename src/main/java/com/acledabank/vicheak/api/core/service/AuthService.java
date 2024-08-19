package com.acledabank.vicheak.api.core.service;

import com.acledabank.vicheak.api.core.dto.*;
import jakarta.mail.MessagingException;

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

    /**
     * This method is used to register a user for the role SUBSCRIBER
     * @param registerDto is the request from client
     * @throws MessagingException
     */
    void register(RegisterDto registerDto) throws MessagingException;

    /**
     * This method is used to verify the email via verification code
     * @param verifyDto is the request from client
     */
    void verify(VerifyDto verifyDto);

    /**
     * This method is used when the client forgets the password to the system
     * @param forgetPasswordDto is the request from client
     * @return PasswordTokenDto
     * @throws MessagingException
     */
    PasswordTokenDto forgetPassword(ForgetPasswordDto forgetPasswordDto) throws MessagingException;

    /**
     * This method is used to send verification code to client's email
     * @param email is the request from client
     * @throws MessagingException
     */
    void sendVerificationCode(String email) throws MessagingException;

    /**
     * This method is used to reset client's password after verify the account
     * @param resetPasswordDto is the request from client
     */
    void resetPassword(ResetPasswordDto resetPasswordDto);

}
