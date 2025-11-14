package com.buccodev.banking_service.exceptions.card;

public class CardLimitsException extends RuntimeException {
    public CardLimitsException(String message) {
        super(message);
    }
}
