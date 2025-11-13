package com.buccodev.banking_service.services;

import com.buccodev.banking_service.entities.Account;
import com.buccodev.banking_service.entities.PixType;
import com.buccodev.banking_service.exception.AccountNotFoundException;
import com.buccodev.banking_service.repositories.AccountRepository;
import com.buccodev.banking_service.utils.dto.account.AccountResponseDto;
import com.buccodev.banking_service.utils.dto.account.PixPaymentRequestDto;
import com.buccodev.banking_service.utils.mapper.AccountMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        account.setBalance(account.getBalance().subtract(amount));
        accountDestination.setBalance(accountDestination.getBalance().add(amount));
        accountRepository.save(account);
        accountRepository.save(accountDestination);
    }

    public void deleteAccount(Long id) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        accountRepository.deleteById(account.getId());
    }

    public List<AccountResponseDto> getAllAccounts(Integer page, Integer size){

        if (page == null || page < 0 || size == null || size < 0) {
            page = 0;
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts.stream().map(AccountMapper::toAccountResponseDto).toList();
    }

    public void addBalance(Long id, BigDecimal amount) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public void removeBalance(Long id, BigDecimal amount) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    public void updateCreditLimit(Long id, BigDecimal creditLimit) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        account.setCreditLimit(creditLimit);
        accountRepository.save(account);
    }

    public void updatePixKey(Long id, PixType pixType) {
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        if (account.getPixKey() != null) {
            throw new AccountNotFoundException("Account not found");
        }



       var newPix  = switch (pixType){
           case  PixType.CPF -> account.getCustomer().getCpf();
           case PixType.EMAIL -> account.getCustomer().getEmail();
           case PixType.PHONE -> account.getCustomer().getPhone()
                   .equals("00000000000") ? null : account.getCustomer().getPhone();
       };
       account.setPixKey(newPix);

        accountRepository.save(account);
    }

}
