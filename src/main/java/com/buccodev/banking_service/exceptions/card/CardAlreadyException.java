package com.buccodev.banking_service.exceptions.card;

public class CardAlreadyException extends RuntimeException {
    public CardAlreadyException(String message) {
        super(message);
    }
}
