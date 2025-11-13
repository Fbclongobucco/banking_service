package com.buccodev.banking_service.exception;

public class CardAlreadyException extends RuntimeException {
    public CardAlreadyException(String message) {
        super(message);
    }
}
