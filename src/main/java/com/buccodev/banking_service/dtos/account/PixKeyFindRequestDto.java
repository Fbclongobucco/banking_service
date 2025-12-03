package com.buccodev.banking_service.dtos.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PixKeyFindRequestDto(
        @NotBlank(message = "the destination pix cannot be empty") String pixKey) {
}
