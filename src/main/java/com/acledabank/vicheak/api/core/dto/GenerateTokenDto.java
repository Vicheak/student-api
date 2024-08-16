package com.acledabank.vicheak.api.core.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.Instant;

@Builder
public record GenerateTokenDto(String auth,
                               String scope,
                               String previousToken,
                               Instant expiration,
                               Duration duration,
                               Integer checkDurationNumber) {
}