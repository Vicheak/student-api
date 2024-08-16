package com.acledabank.vicheak.api.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshTokenDto(@NotBlank(message = "Refresh Token should not be blank!")
                              String refreshToken) {
}

