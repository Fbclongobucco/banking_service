package com.buccodev.banking_service.exceptions.auth;

public class JWTException extends RuntimeException {
    public JWTException(String message) {
        super(message);
    }
}
