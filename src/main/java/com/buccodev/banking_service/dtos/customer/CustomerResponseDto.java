package com.buccodev.banking_service.dtos.customer;


public record CustomerResponseDto(Long id, String name, String email, String cpf,
                                  String phone, AccountCustomerResponseDto account) {
                                }
