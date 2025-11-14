package com.buccodev.banking_service.repositories;

import com.buccodev.banking_service.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);

    @NonNull
    @Override
    @EntityGraph(attributePaths = {
            "account",
            "account.card"
    })
    Page<Customer> findAll(@NonNull Pageable pageable);

}
