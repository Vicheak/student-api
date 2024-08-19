package com.acledabank.vicheak.api.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ResetPasswordDto(@NotBlank(message = "User's email must not be blank!")
                               @Email(message = "User's email must be a valid email address!")
                               @Size(max = 50, message = "User's email maximum character is 150 characters!")
                               String email,

                               @NotBlank(message = "Password token must not be blank!")
                               String token,

                               @NotBlank(message = "User's password must not be blank!")
                               @Size(min = 8, message = "User's password must be at least 8 characters!")
                               String password,

                               @NotBlank(message = "User's password confirmation must not be blank!")
                               @Size(min = 8, message = "User's password confirmation must be at least 8 characters!")
                               String passwordConfirmation) {
}

