package com.buccodev.banking_service.exception;

public class CustomerAlreadyRegisteredException extends RuntimeException {
    public CustomerAlreadyRegisteredException(String message) {
        super(message);
    }
}
