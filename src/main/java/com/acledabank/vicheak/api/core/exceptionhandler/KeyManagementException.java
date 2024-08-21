package com.acledabank.vicheak.api.core.exceptionhandler;

public class KeyManagementException extends RuntimeException {

    public KeyManagementException(String message) {
        super(message);
    }

    public KeyManagementException(String message, Throwable cause) {
        super(message, cause);
    }

}
