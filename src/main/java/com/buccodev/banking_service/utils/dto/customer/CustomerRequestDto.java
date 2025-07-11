package com.buccodev.banking_service.utils.dto.customer;


public record CustomerRequestDto(String name, String email, String cpf, String password, String phone) {
}
