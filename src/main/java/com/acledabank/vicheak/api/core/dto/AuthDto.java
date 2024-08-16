package com.acledabank.vicheak.api.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthDto(@NotBlank(message = "Username cannot be blank!")
                      String username,

                      @NotBlank(message = "Password cannot be blank!")
                      String password) {
}
