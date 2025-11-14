package com.buccodev.banking_service.services;

import com.buccodev.banking_service.dtos.sharedDtos.PageResponseDto;
import com.buccodev.banking_service.entities.Account;
import com.buccodev.banking_service.entities.CardType;
import com.buccodev.banking_service.entities.PixType;
import com.buccodev.banking_service.exceptions.account.AccountNotFoundException;
import com.buccodev.banking_service.exceptions.account.BalanceNotEnoughException;
import com.buccodev.banking_service.exceptions.card.CardLimitsException;
import com.buccodev.banking_service.repositories.AccountRepository;
import com.buccodev.banking_service.dtos.account.AccountResponseDto;
import com.buccodev.banking_service.dtos.account.PixPaymentRequestDto;
import com.buccodev.banking_service.dtos.account.UpdatePixDto;
import com.buccodev.banking_service.utils.mapper.AccountMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponseDto getAccountById(Long id) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        return AccountMapper.toAccountResponseDto(account);
    }

    public AccountResponseDto getAccountByAccountNumber(String accountNumber) {
        var account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));
        return AccountMapper.toAccountResponseDto(account);
    }

    @Transactional
    public void paymentWithPix(Long id, PixPaymentRequestDto pixPaymentRequestDto) {
        var pixKeyDestination = pixPaymentRequestDto.pixKeyDestination();
        var amount = pixPaymentRequestDto.amount();
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        var accountDestination = accountRepository.findByPixKey(pixKeyDestination)
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BalanceNotEnoughException("Balance not enough");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountDestination.setBalance(accountDestination.getBalance().add(amount));
        accountRepository.save(account);
        accountRepository.save(accountDestination);
    }

    public void deleteAccount(Long id) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        accountRepository.deleteById(account.getId());
    }

    public PageResponseDto<AccountResponseDto> getAllAccounts(Integer page, Integer size){

        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 10;

       Page<Account> accounts = accountRepository.findAll(PageRequest.of(page, size));
       List<AccountResponseDto> content = accounts.getContent().stream()
               .map(AccountMapper::toAccountResponseDto)
               .toList();
       return new PageResponseDto<>(
               content,
               accounts.getNumber(),
               accounts.getSize(),
               accounts.getTotalElements(),
               accounts.getTotalPages(),
               accounts.isFirst(),
               accounts.isLast()
       );
    }

    public void addBalance(Long id, BigDecimal amount) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public void removeBalance(Long id, BigDecimal amount) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BalanceNotEnoughException("Balance not enough");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    public void updateCreditLimit(Long id, BigDecimal creditLimit) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        if(account.getCard().getCardType() != CardType.CREDIT_CARD){
            throw new CardLimitsException("Card is not a credit card");
        }
        account.setCreditLimit(creditLimit);
        accountRepository.save(account);
    }

    public void updatePixKey(Long id, UpdatePixDto updatePixDto) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));

       var newPix  = switch (updatePixDto.pixType()){
           case  PixType.CPF -> account.getCustomer().getCpf();
           case PixType.EMAIL -> account.getCustomer().getEmail();
           case PixType.PHONE -> account.getCustomer().getPhone()
                   .equals("00000000000") ? null : account.getCustomer().getPhone();

       };
       account.setPixKey(newPix);

        accountRepository.save(account);
    }

}
