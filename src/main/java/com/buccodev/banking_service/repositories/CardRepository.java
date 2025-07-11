package com.buccodev.banking_service.repositories;

import com.buccodev.banking_service.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumber(String cardNumber);
}
