package com.buccodev.banking_service.dtos.card;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardResponseDto(LocalDate datePayment, BigDecimal amount, Integer installments,
                                    BigDecimal interestRate, BigDecimal totalAmount) {
}
