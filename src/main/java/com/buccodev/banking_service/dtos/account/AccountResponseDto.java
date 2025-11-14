package com.buccodev.banking_service.dtos.account;

import java.math.BigDecimal;

public record AccountResponseDto(Long id, String accountNumber,  BigDecimal balance, BigDecimal limitCredit,
                                 String pixKey, CustomerAccountResponseDto customer, CardAccountResponseDto card) {
                                }
