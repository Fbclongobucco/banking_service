package com.buccodev.banking_service.controllers;

import com.buccodev.banking_service.dtos.sharedDtos.PageResponseDto;
import com.buccodev.banking_service.services.AccountService;
import com.buccodev.banking_service.dtos.account.AccountResponseDto;
import com.buccodev.banking_service.dtos.account.PixPaymentRequestDto;
import com.buccodev.banking_service.dtos.account.UpdatePixDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long id) {
        var accountDto = accountService.getAccountById(id);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/num-account/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByAccountNumber(@PathVariable String accountNumber) {
        var accountDto = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/pix/{id}")
    public ResponseEntity<Void> paymentWithPix(@PathVariable Long id,
                                               @Valid
                                               @RequestBody PixPaymentRequestDto pixPaymentRequestDto
    ) {
        accountService.paymentWithPix(id, pixPaymentRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<PageResponseDto<AccountResponseDto>> getAllAccounts(@RequestParam(defaultValue = "0") Integer page,
                                                                              @RequestParam(defaultValue = "10") Integer size) {
        var accountsDto = accountService.getAllAccounts(page, size);
        return ResponseEntity.ok(accountsDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/add-balance/{amount}")
    public ResponseEntity<Void> addBalance(@PathVariable Long id, @PathVariable BigDecimal amount) {
        accountService.addBalance(id, amount);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/remove-balance/{amount}")
    public ResponseEntity<Void> removeBalance(@PathVariable Long id, @PathVariable BigDecimal amount) {
        accountService.removeBalance(id, amount);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/update-credit-limit/{creditLimit}")
    public ResponseEntity<Void> updateCreditLimit(@PathVariable Long id, @PathVariable BigDecimal creditLimit) {
        accountService.updateCreditLimit(id, creditLimit);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-pix-key/{id}")
    public ResponseEntity<Void> updatePixKey(@PathVariable Long id, @Valid @RequestBody UpdatePixDto updatePixDto) {
        accountService.updatePixKey(id, updatePixDto);
        return ResponseEntity.ok().build();
    }
}
