package com.buccodev.banking_service.utils.dto.account;

public record CustomerAccountResponseDto(Long id, String name, String email, String cpf, String password,
                                         String phone ) {
}
