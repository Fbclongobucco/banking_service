package com.buccodev.banking_service.utils.mapper;

import com.buccodev.banking_service.entities.Account;
import com.buccodev.banking_service.utils.dto.account.AccountResponseDto;

public class AccountMapper {

    public static AccountResponseDto toAccountResponseDto(Account account) {
        return new AccountResponseDto(
                account.getId(),
                CustomerMapper.toCustomerResponseDto(account.getCustomer()),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCard()
        );
    }
}
