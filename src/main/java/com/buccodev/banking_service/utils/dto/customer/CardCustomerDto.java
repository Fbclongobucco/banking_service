package com.buccodev.banking_service.utils.dto.customer;

import java.time.LocalDate;

public record CardCustomerDto(Long id, String cardNumber, String cvv, LocalDate expirationDate) {
}
