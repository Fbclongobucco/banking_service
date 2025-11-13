package com.buccodev.banking_service.utils.dto.account;

import java.time.LocalDate;

public record CardAccountResponseDto(Long id, String cardNumber,
                                     String cvv, LocalDate expirationDate) {
}
