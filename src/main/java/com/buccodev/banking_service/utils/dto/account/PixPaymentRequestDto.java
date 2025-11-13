package com.buccodev.banking_service.utils.dto.account;

import java.math.BigDecimal;

public record PixPaymentRequestDto(String pixKeyDestination, BigDecimal amount) {
}
