package com.buccodev.banking_service.entities.cards;

import com.buccodev.banking_service.entities.Account;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
public class DebitAndCreditCard extends Card {

    private BigDecimal creditLimit = BigDecimal.ZERO;
    private BigDecimal creditBalance = BigDecimal.ZERO;

    public DebitAndCreditCard(Long id, Account account, String cardNumber, String cvv, LocalDate expirationDate) {
        super(id, account, cardNumber, cvv, expirationDate);
    }

    @Override
    public boolean debitPayment(BigDecimal value) {
        if (super.getAccount().getBalance().compareTo(value) >= 0) {
            super.getAccount().setBalance(super.getAccount().getBalance().subtract(value));
            return true;
        }
        return false;
    }

    @Override
    public boolean depositAmount(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) > 0) {
            super.getAccount().setBalance(super.getAccount().getBalance().add(value));
            return true;
        }
        return false;
    }


    public boolean creditPayment(BigDecimal value, Integer installments) {

        if (value.compareTo(BigDecimal.ZERO) <= 0 || installments <= 0) {
            return false;
        }

        BigDecimal monthlyInterestRate = new BigDecimal("0.02");

        BigDecimal totalWithInterest = value
                .add(value.multiply(monthlyInterestRate).multiply(BigDecimal.valueOf(installments)))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal availableCredit = creditLimit.subtract(creditBalance);

        if (availableCredit.compareTo(totalWithInterest) >= 0) {
            creditBalance = creditBalance.add(totalWithInterest);
            return true;
        }

        return false;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
    }

    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(creditBalance);
    }
}
