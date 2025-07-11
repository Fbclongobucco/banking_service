package com.buccodev.banking_service.repositories;

import com.buccodev.banking_service.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String numAccount);
}
