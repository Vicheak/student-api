package com.acledabank.vicheak.api.core.dto;

import lombok.Builder;

@Builder
public record AuthResponseDto(String accessToken,
                              String refreshToken,
                              String type) {
}
