package com.buccodev.banking_service.utils.mapper;

import com.buccodev.banking_service.entities.Customer;
import com.buccodev.banking_service.dtos.customer.AccountCustomerResponseDto;
import com.buccodev.banking_service.dtos.customer.CardCustomerDto;
import com.buccodev.banking_service.dtos.customer.CustomerResponseDto;

public class CustomerMapper {

    public static CustomerResponseDto toCustomerResponseDto(Customer customer) {

        var cardDto = new CardCustomerDto(
                customer.getAccount().getCard().getId(),
                customer.getAccount().getCard().getCardNumber(),
                customer.getAccount().getCard().getCvv(),
                customer.getAccount().getCard().getExpirationDate()
        );

        var accountDto = new AccountCustomerResponseDto(
                customer.getAccount().getId(),
                customer.getAccount().getAccountNumber(),
                customer.getAccount().getBalance(),
                customer.getAccount().getCreditLimit(),
                customer.getAccount().getPixKey(),
                cardDto
        );

        return new CustomerResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCpf(),
                customer.getPhone(),
                accountDto
        );
    }
}
