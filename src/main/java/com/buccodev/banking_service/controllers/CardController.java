package com.buccodev.banking_service.controllers;

import com.buccodev.banking_service.services.CardService;
import com.buccodev.banking_service.utils.dto.card.CardResponseDto;
import com.buccodev.banking_service.utils.dto.card.CreditCardRequestDto;
import com.buccodev.banking_service.utils.dto.card.CreditCardResponseDto;
import com.buccodev.banking_service.utils.dto.card.DebitCardRequestDto;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/payment/debit/{amount}")
    public ResponseEntity<Void> paymentDebit(@PathVariable BigDecimal amount,
                                             @RequestBody DebitCardRequestDto cardRequestDto){
        cardService.paymentDebit(cardRequestDto, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/payment/credit/{amount}")
    public ResponseEntity<CreditCardResponseDto> paymentCredit(@PathVariable BigDecimal amount,
                                                               @RequestBody CreditCardRequestDto cardRequestDto){
        return ResponseEntity.ok(cardService.paymentCredit(cardRequestDto, amount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> getCardById(@PathVariable Long id){
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<CardResponseDto> cancelAndCreateNewCard(@PathVariable Long id){
        return ResponseEntity.ok(cardService.cancelAndCreateNewCard(id));
    }

}
