package com.buccodev.banking_service.exceptions.account;

public class AccountConflictException extends RuntimeException {
    public AccountConflictException(String message) {
        super(message);
    }
}
