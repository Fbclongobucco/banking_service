package com.buccodev.banking_service.utils.dto.account;

import com.buccodev.banking_service.entities.Card;
import com.buccodev.banking_service.entities.Customer;
import com.buccodev.banking_service.utils.dto.customer.CustomerResponseDto;

import java.math.BigDecimal;

public record AccountResponseDto(Long id, CustomerResponseDto customer, String accountNumber, BigDecimal balance, Card card) {
}
