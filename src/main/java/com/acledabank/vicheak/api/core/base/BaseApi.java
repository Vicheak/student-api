package com.acledabank.vicheak.api.core.base;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BaseApi<T>(Boolean isSuccess,
                         Integer code,
                         String message,
                         LocalDateTime timestamp,
                         T payload) {
}