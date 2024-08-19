package com.acledabank.vicheak.api.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record IsEnabledDto(@NotNull(message = "User status must not be null!")
                           Boolean isEnabled) {
}

