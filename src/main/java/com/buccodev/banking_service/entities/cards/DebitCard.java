package com.buccodev.banking_service.entities.cards;

import com.buccodev.banking_service.entities.Account;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class DebitCard extends Card {


    public DebitCard(Long id, Account account, String cardNumber, String cvv, LocalDate expirationDate) {
        super(id, account, cardNumber, cvv, expirationDate);
    }

    @Override
    public boolean debitPayment(BigDecimal value) {

        if(super.getAccount().getBalance().compareTo(value) >= 0) {
            super.getAccount().setBalance(super.getAccount().getBalance().subtract(value));
            return true;
        }

        return false;
    }

    @Override
    public boolean depositAmount(BigDecimal value) {

        if(value.compareTo(BigDecimal.ZERO) > 0) {
            super.getAccount().setBalance(super.getAccount().getBalance().add(value));
            return true;
        }

        return false;
    }


}
