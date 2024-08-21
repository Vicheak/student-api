package com.acledabank.vicheak.api.core.exceptionhandler;

import com.acledabank.vicheak.api.core.base.BaseError;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class PropertyHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PropertyReferenceException.class)
    public BaseError<Object> handlePropertyException(PropertyReferenceException ex) {
        return BaseError.builder()
                .isSuccess(false)
                .message("Something went wrong, please check...!")
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .payload(Map.of("errorReason", "There is no such property!"))
                .build();
    }

}