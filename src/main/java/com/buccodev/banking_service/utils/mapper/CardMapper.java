package com.buccodev.banking_service.utils.mapper;

import com.buccodev.banking_service.entities.Card;
import com.buccodev.banking_service.utils.dto.card.CardResponseDto;

public class CardMapper {

    public static CardResponseDto toCardResponseDto(Card card) {
        return new CardResponseDto(card.getId(), card.getAccount().getCustomer().getName(), card.getAccount().getAccountNumber(),
                card.getCardNumber(), card.getCvv(), card.getExpirationDate());
    }
}
