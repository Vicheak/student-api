package com.acledabank.vicheak.api.core.exception;

import com.acledabank.vicheak.api.core.base.BaseError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ServiceException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleServiceException(ResponseStatusException ex) {
        return new ResponseEntity<>(BaseError.builder()
                .isSuccess(false)
                .message("Something went wrong, please check...!")
                .code(ex.getStatusCode().value())
                .timestamp(LocalDateTime.now())
                .payload(Map.of("errorReason", Objects.requireNonNull(ex.getReason())))
                .build(), ex.getStatusCode());
    }

}