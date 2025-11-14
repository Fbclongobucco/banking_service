package com.buccodev.banking_service.dtos.card;

import java.time.LocalDate;

public record CreditCardRequestDto(String number, String cvv, LocalDate expirationDate,  Integer installments, String accountNumberDestination) {
}
