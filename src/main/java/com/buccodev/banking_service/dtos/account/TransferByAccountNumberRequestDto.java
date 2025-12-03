package com.buccodev.banking_service.dtos.account;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record TransferByAccountNumberRequestDto(@NotBlank String accountNumber, BigDecimal amount) {
}
