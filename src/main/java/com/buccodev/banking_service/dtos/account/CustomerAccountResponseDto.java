package com.buccodev.banking_service.dtos.account;

public record CustomerAccountResponseDto(Long id, String name, String email, String cpf,
                                         String phone ) {
}
