package com.acledabank.vicheak.api.core.dto;

import lombok.Builder;

@Builder
public record PasswordTokenDto(String message,
                               String token) {
}
