package com.acledabank.vicheak.api.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record VerifyDto(@NotBlank(message = "User's email must not be blank!")
                        @Email(message = "User's email must be a valid email address!")
                        @Size(max = 50, message = "User's email maximum character is 150 characters!")
                        String email,

                        @NotBlank(message = "Verified code should not be blank!")
                        @Size(min = 6, message = "Verified code must be a six-digit code!")
                        String verifiedCode) {
}
