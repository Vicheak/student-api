package com.acledabank.vicheak.api.core.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BaseError<T>(Boolean isSuccess,
                           Integer code,
                           @JsonInclude(JsonInclude.Include.NON_NULL)
                           String message,
                           LocalDateTime timestamp,
                           T payload) {
}
