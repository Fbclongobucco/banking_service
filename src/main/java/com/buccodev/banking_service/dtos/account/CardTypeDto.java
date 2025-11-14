package com.buccodev.banking_service.dtos.account;

import com.buccodev.banking_service.entities.CardType;
import jakarta.validation.constraints.NotNull;

public record CardTypeDto(@NotNull(message = "the card type cannot be null!") CardType cardType) {
}
