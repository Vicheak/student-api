package com.acledabank.vicheak.api.core.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.Set;

@Builder
public record TransactionUserDto(@NotBlank(message = "User's first name must not be blank!")
                                 @Size(max = 50, message = "User's first name maximum character is 50 characters!")
                                 String firstName,

                                 @NotBlank(message = "User's last name must not be blank!")
                                 @Size(max = 50, message = "User's last name maximum character is 50 characters!")
                                 String lastName,

                                 @NotBlank(message = "User's email must not be blank!")
                                 @Email(message = "User's email must be a valid email address!")
                                 @Size(max = 50, message = "User's email maximum character is 150 characters!")
                                 String email,

                                 @NotBlank(message = "User's password must not be blank!")
                                 @Size(min = 8, message = "User's password must be at least 8 characters!")
                                 String password,

                                 @NotBlank(message = "User's phone number must not be blank!")
                                 @Size(max = 50, message = "User's phone number maximum character is 50 characters!")
                                 String phoneNumber,

                                 @NotEmpty(message = "User's roles must not be empty!")
                                 @Size(min = 1, message = "User must have at least one role!")
                                 Set<@NotNull(message = "Role ID must not be null!") @Positive(message = "Role ID must be positive!")
                                         Integer> roleIds) {
}
