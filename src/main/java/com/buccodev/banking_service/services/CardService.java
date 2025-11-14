package com.buccodev.banking_service.services;

import com.buccodev.banking_service.entities.Card;
import com.buccodev.banking_service.entities.CardType;
import com.buccodev.banking_service.exceptions.account.AccountNotFoundException;
import com.buccodev.banking_service.exceptions.card.CardLimitsException;
import com.buccodev.banking_service.exceptions.card.CardNotFoundException;
import com.buccodev.banking_service.repositories.AccountRepository;
import com.buccodev.banking_service.repositories.CardRepository;
import com.buccodev.banking_service.dtos.account.CardTypeDto;
import com.buccodev.banking_service.dtos.card.CardResponseDto;
import com.buccodev.banking_service.dtos.card.CreditCardRequestDto;
import com.buccodev.banking_service.dtos.card.CreditCardResponseDto;
import com.buccodev.banking_service.dtos.card.DebitCardRequestDto;
import com.buccodev.banking_service.utils.mapper.CardMapper;
import com.buccodev.banking_service.utils.num_generate.CardNumGenerate;
import jakarta.transaction.Transactional;
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
    @Transactional
    public void paymentDebit(DebitCardRequestDto cardRequestDto, BigDecimal amount) {
        var card = cardRepository.findByCardNumber(cardRequestDto.number())
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        if (!validateDebitCard(card, cardRequestDto.cvv(), cardRequestDto.expirationDate())) {
            throw new CardNotFoundException("Invalid card");
        }

        var account = accountRepository.findById(card.getAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if(account.getBalance().compareTo(amount) < 0){
            throw new AccountNotFoundException("Account balance is not enough");
        }

        var accountDestination = accountRepository.findByAccountNumber(cardRequestDto.accountNumberDestination())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        accountDestination.setBalance(accountDestination.getBalance().add(amount));

        account.setBalance(account.getBalance().subtract(amount));

        accountRepository.save(account);
        accountRepository.save(accountDestination);
    }

    public CreditCardResponseDto paymentCredit(CreditCardRequestDto cardRequestDto, BigDecimal amount) {
        var card = cardRepository.findByCardNumber(cardRequestDto.number())
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        if (!validateCreditCard(card, cardRequestDto.cvv(), cardRequestDto.expirationDate())) {
            throw new CardNotFoundException("Invalid card");
        }

        var account = accountRepository.findById(card.getAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        var accountDestination = accountRepository.findByAccountNumber(cardRequestDto.accountNumberDestination())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if ( account.getCreditLimit().compareTo(amount) < 0) {
            throw new AccountNotFoundException("Account balance is not enough or credit limit is not enough");
        }

        accountDestination.setBalance(accountDestination.getBalance().add(amount));


        var installments = cardRequestDto.installments();

        if (installments > 10) {
            throw new CardLimitsException("Installments is not allowed");
        }
        BigDecimal interestRate = switch (installments) {
            case 1 -> BigDecimal.ZERO;
            case 2 -> BigDecimal.valueOf(0.04);
            case 3 -> BigDecimal.valueOf(0.06);
            default -> BigDecimal.valueOf(0.08);
        };

        var totalAmount = amount.add(amount.multiply(interestRate).multiply(BigDecimal.valueOf(installments)));

        account.setCreditLimit(account.getCreditLimit().subtract(totalAmount));
        accountRepository.save(account);
        accountRepository.save(accountDestination);

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

        var password = CardNumGenerate.generatePassword();

        var newCard = new Card(null, account, cardNumber, cvv, expirationDate, password);
        newCard.setCardType(cardOld.getCardType());

        cardRepository.save(newCard);
        return CardMapper.toCardResponseDto(newCard);
    }

    public void updateCardType(Long id, CardTypeDto cardTypeDto){
        var account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account not found"));
        account.getCard().setCardType(cardTypeDto.cardType());
        accountRepository.save(account);
    }

    private boolean validateDebitCard(Card card, String requestCvv, LocalDate requestExpirationDate) {
        if (card == null || card.getCvv() == null) return false;

        if (!card.getCvv().equals(requestCvv) || !card.getExpirationDate().equals(requestExpirationDate)) {
            return false;
        }

        LocalDate now = LocalDate.now();
        LocalDate expiration = card.getExpirationDate();

        return expiration.getYear() >= now.getYear() &&
                (expiration.getYear() != now.getYear() || expiration.getMonthValue() >= now.getMonthValue());
    }


    private boolean validateCreditCard(Card card, String requestCvv, LocalDate requestExpirationDate) {
        if (card == null || card.getCvv() == null) return false;

        if (card.getCardType() != CardType.CREDIT_CARD) {
            return false;
        }

        if (!card.getCvv().equals(requestCvv) || !card.getExpirationDate().equals(requestExpirationDate)) {
            return false;
        }

        LocalDate now = LocalDate.now();
        LocalDate expiration = card.getExpirationDate();

        return expiration.getYear() >= now.getYear() &&
                (expiration.getYear() != now.getYear() || expiration.getMonthValue() >= now.getMonthValue());
    }

}
