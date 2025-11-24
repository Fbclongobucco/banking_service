package com.buccodev.banking_service.exceptions;

public class CredentialInvalidException extends RuntimeException {
    public CredentialInvalidException(String message) {
        super(message);
    }
}
