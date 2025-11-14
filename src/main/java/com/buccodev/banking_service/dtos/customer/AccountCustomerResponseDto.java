package com.buccodev.banking_service.dtos.customer;

import java.math.BigDecimal;

public record AccountCustomerResponseDto(Long id, String accountNumber, BigDecimal balance,
                                         BigDecimal creditLimit, String pixKey, CardCustomerDto card) {
}
