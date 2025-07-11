package com.buccodev.banking_service.utils.dto.customer;

import com.buccodev.banking_service.utils.dto.account.AccountResponseDto;

public record CustomerResponseDto(Long id, String name, String email, String cpf, String password, String phone, AccountResponseDto account) {
}
