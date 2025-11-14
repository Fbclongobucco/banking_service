package com.buccodev.banking_service.dtos.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PixPaymentRequestDto(@NotBlank(message = "the destination pix cannot be empty") String pixKeyDestination,
                                   @NotNull(message = "the amount cannot be null") BigDecimal amount) {
}
