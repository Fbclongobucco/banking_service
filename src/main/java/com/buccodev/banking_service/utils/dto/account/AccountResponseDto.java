package com.buccodev.banking_service.utils.dto.account;

import com.buccodev.banking_service.entities.Card;
import com.buccodev.banking_service.entities.Customer;
import com.buccodev.banking_service.utils.dto.card.CardResponseDto;
import com.buccodev.banking_service.utils.dto.customer.CardCustomerDto;
import com.buccodev.banking_service.utils.dto.customer.CustomerResponseDto;

import java.math.BigDecimal;

public record AccountResponseDto(Long id, String accountNumber,  BigDecimal balance, BigDecimal limitCredit,
                                 String pixKay, CustomerAccountResponseDto customer, CardAccountResponseDto card) {
                                }
