package com.buccodev.banking_service.dtos.card;

import java.time.LocalDate;

public record DebitCardRequestDto(String number, String cvv, LocalDate expirationDate, String accountNumberDestination) {
}
