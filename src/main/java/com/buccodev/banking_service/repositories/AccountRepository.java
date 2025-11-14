package com.buccodev.banking_service.repositories;

import com.buccodev.banking_service.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String numAccount);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByPixKey(String pixKeyDestination);

    @NonNull
    @EntityGraph(attributePaths = {
            "customer",
            "card"
    })
    Page<Account> findAll(@NonNull Pageable pageable);
}
