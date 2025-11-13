package com.buccodev.banking_service.repositories;

import com.buccodev.banking_service.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String numAccount);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByPixKey(String pixKeyDestination);
}
