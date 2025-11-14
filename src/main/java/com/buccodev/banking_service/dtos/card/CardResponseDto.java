package com.buccodev.banking_service.dtos.card;

import com.buccodev.banking_service.entities.CardType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardResponseDto(Long id, String customerName, String accountNumber, String cardNumber,
                              String cvv, LocalDate expirationDate, BigDecimal limitCredit, CardType cardType) {
}
