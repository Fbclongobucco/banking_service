package com.buccodev.banking_service.utils.dto.card;

import com.buccodev.banking_service.utils.dto.account.AccountResponseDto;

import java.time.LocalDate;

public record CardResponseDto(Long id, AccountResponseDto account, String cardNumber, String cvv, LocalDate expirationDate) {
}
