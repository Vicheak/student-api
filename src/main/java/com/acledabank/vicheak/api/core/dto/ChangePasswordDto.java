package com.acledabank.vicheak.api.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChangePasswordDto(@NotBlank(message = "Old password must not be blank!")
                                @Size(min = 8, message = "Old password must be at least 8 characters!")
                                String oldPassword,

                                @NotBlank(message = "Password must not be blank!")
                                @Size(min = 8, message = "Password must be at least 8 characters!")
                                String password,

                                @NotBlank(message = "Password confirmation must not be blank!")
                                @Size(min = 8, message = "Password confirmation must be at least 8 characters!")
                                String passwordConfirmation) {
}
