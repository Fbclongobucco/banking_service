package com.buccodev.banking_service.utils.mapper;

import com.buccodev.banking_service.entities.Account;
import com.buccodev.banking_service.dtos.account.AccountResponseDto;
import com.buccodev.banking_service.dtos.account.CardAccountResponseDto;
import com.buccodev.banking_service.dtos.account.CustomerAccountResponseDto;

public class AccountMapper {

    public static AccountResponseDto toAccountResponseDto(Account account) {

        var cardDto = new CardAccountResponseDto(
                account.getCard().getId(),
                account.getCard().getCardNumber(),
                account.getCard().getCvv(),
                account.getCard().getExpirationDate()
        );

        var customerDto = new CustomerAccountResponseDto(
                account.getCustomer().getId(),
                account.getCustomer().getName(),
                account.getCustomer().getEmail(),
                account.getCustomer().getCpf(),
                account.getCustomer().getPhone()
        );

        return new AccountResponseDto(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCreditLimit(),
                account.getPixKey(),
                customerDto,
                cardDto
        );

    }
}
