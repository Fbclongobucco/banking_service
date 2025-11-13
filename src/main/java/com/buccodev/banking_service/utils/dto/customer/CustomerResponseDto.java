package com.buccodev.banking_service.utils.dto.customer;


public record CustomerResponseDto(Long id, String name, String email, String cpf, String password,
                                  String phone, AccountCustomerResponseDto account) {
                                }
