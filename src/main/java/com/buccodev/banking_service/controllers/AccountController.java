package com.buccodev.banking_service.controllers;

import com.buccodev.banking_service.entities.PixType;
import com.buccodev.banking_service.services.AccountService;
import com.buccodev.banking_service.utils.dto.account.AccountResponseDto;
import com.buccodev.banking_service.utils.dto.account.PixPaymentRequestDto;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long id) {
        var accountDto = accountService.getAccountById(id);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByAccountNumber(@PathVariable String accountNumber) {
        var accountDto = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/pix/{id}/{pixKeyDestination}/{amount}")
    public ResponseEntity<Void> paymentWithPix(@PathVariable Long id,
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

    @GetMapping("/all")
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts(@PathParam("page") Integer page, @PathParam("size") Integer size) {
        var accountsDto = accountService.getAllAccounts(page, size);
        return ResponseEntity.ok(accountsDto);
    }

    @PutMapping("/{id}/add-balance/{amount}")
    public ResponseEntity<Void> addBalance(@PathVariable Long id, @PathVariable BigDecimal amount) {
        accountService.addBalance(id, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/remove-balance/{amount}")
    public ResponseEntity<Void> removeBalance(@PathVariable Long id, @PathVariable BigDecimal amount) {
        accountService.removeBalance(id, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/update-credit-limit/{creditLimit}")
    public ResponseEntity<Void> updateCreditLimit(@PathVariable Long id, @PathVariable BigDecimal creditLimit) {
        accountService.updateCreditLimit(id, creditLimit);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/update-pix-key/{pixKey}")
    public ResponseEntity<Void> updatePixKey(@PathVariable Long id, @PathVariable PixType pixKey) {
        accountService.updatePixKey(id, pixKey);
        return ResponseEntity.ok().build();
    }
}
