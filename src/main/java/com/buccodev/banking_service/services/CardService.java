package com.buccodev.banking_service.services;

import com.buccodev.banking_service.entities.Card;
import com.buccodev.banking_service.entities.CardType;
import com.buccodev.banking_service.exception.AccountNotFoundException;
import com.buccodev.banking_service.exception.CardNotFoundException;
import com.buccodev.banking_service.repositories.AccountRepository;
import com.buccodev.banking_service.repositories.CardRepository;
import com.buccodev.banking_service.utils.dto.card.CardResponseDto;
import com.buccodev.banking_service.utils.dto.card.CreditCardRequestDto;
import com.buccodev.banking_service.utils.dto.card.CreditCardResponseDto;
import com.buccodev.banking_service.utils.dto.card.DebitCardRequestDto;
import com.buccodev.banking_service.utils.mapper.CardMapper;
import com.buccodev.banking_service.utils.num_generate.CardNumGenerate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    public void paymentDebit(DebitCardRequestDto cardRequestDto, BigDecimal amount) {
        var card = cardRepository.findByCardNumber(cardRequestDto.number())
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        if (!validateDebitCard(card, cardRequestDto.cvv())) {
            throw new CardNotFoundException("Invalid card");
        }

        var account = accountRepository.findById(card.getAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    public CreditCardResponseDto paymentCredit(CreditCardRequestDto cardRequestDto, BigDecimal amount) {
        var card = cardRepository.findByCardNumber(cardRequestDto.number())
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        if (!validateCreditCard(card, cardRequestDto.cvv())) {
            throw new CardNotFoundException("Invalid card");
        }

        var account = accountRepository.findById(card.getAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0 || account.getCreditLimit().compareTo(amount) < 0) {
            throw new AccountNotFoundException("Account balance is not enough");
        }


        var installments = cardRequestDto.installments();
        BigDecimal interestRate = switch (installments) {
            case 1 -> BigDecimal.ZERO;
            case 2 -> BigDecimal.valueOf(0.05);
            case 3 -> BigDecimal.valueOf(0.1);
            default -> BigDecimal.valueOf(0.15);
        };

        var totalAmount = amount.add(amount.multiply(interestRate).multiply(BigDecimal.valueOf(installments)));

        account.setBalance(account.getBalance().subtract(totalAmount));
        account.setCreditLimit(account.getCreditLimit().subtract(amount));
        accountRepository.save(account);

        return new CreditCardResponseDto(LocalDate.now(), amount, installments, interestRate, totalAmount);
    }

    public CardResponseDto getCardById(Long id) {
        var card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Card not found"));
        return CardMapper.toCardResponseDto(card);
    }

    public CardResponseDto cancelAndCreateNewCard(Long id) {
        var cardOld = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Card not found"));
        var account = accountRepository.findById(cardOld.getAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        cardRepository.delete(cardOld);

        var cardNumber = CardNumGenerate.generateNumCard();

        if(cardRepository.existsByCardNumber(cardNumber)){
            throw new CardNotFoundException("Card number already exists");
        }
        var cvv = CardNumGenerate.generateCvv();

        var expirationDate = LocalDate.now().plusYears(5);


        var newCard = new Card(null, account, cardNumber, cvv, expirationDate);
        newCard.setCardType(cardOld.getCardType());

        cardRepository.save(newCard);
        return CardMapper.toCardResponseDto(newCard);
    }

    private boolean validateDebitCard(Card card, String cvv) {
        if (card == null || cvv == null) return false;

        if (card.getCardType() != CardType.DEBIT_CARD) {
            return false;
        }

        LocalDate now = LocalDate.now();
        LocalDate expiration = card.getExpirationDate();

        if (expiration.getYear() < now.getYear() ||
                (expiration.getYear() == now.getYear() && expiration.getMonthValue() < now.getMonthValue())) {
            return false;
        }

        return cvv.equals(card.getCvv());
    }


    private boolean validateCreditCard(Card card, String cvv) {
        if (card == null || cvv == null) return false;

        if (card.getCardType() != CardType.CREDIT_CARD) {
            return false;
        }

        LocalDate now = LocalDate.now();
        LocalDate expiration = card.getExpirationDate();

        if (expiration.getYear() < now.getYear() ||
                (expiration.getYear() == now.getYear() && expiration.getMonthValue() < now.getMonthValue())) {
            return false;
        }

        return cvv.equals(card.getCvv());
    }

}
