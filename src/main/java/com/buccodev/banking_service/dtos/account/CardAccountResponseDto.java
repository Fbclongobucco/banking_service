package com.buccodev.banking_service.dtos.account;

import java.time.LocalDate;

public record CardAccountResponseDto(Long id, String cardNumber,
                                     String cvv, LocalDate expirationDate) {
}
