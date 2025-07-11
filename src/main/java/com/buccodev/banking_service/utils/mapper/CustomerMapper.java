package com.buccodev.banking_service.utils.mapper;

import com.buccodev.banking_service.entities.Customer;
import com.buccodev.banking_service.utils.dto.customer.CustomerResponseDto;

public class CustomerMapper {

    public static CustomerResponseDto toCustomerResponseDto(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCpf(),
                customer.getPassword(),
                customer.getPhone(),
                AccountMapper.toAccountResponseDto(customer.getAccount())
        );
    }
}
