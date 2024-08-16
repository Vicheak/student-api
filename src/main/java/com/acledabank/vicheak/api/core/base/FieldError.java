package com.acledabank.vicheak.api.core.base;

import lombok.Builder;

@Builder
public record FieldError(String fieldName,
                         String message) {
}
