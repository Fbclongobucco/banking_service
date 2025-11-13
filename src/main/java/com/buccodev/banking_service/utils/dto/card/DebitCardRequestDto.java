package com.buccodev.banking_service.utils.dto.card;

import java.time.LocalDate;

public record DebitCardRequestDto(String number, String cvv, LocalDate expirationDate) {
}
