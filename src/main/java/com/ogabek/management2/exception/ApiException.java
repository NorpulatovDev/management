package com.ogabek.management2.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
