package com.buccodev.banking_service.dtos.auth;

public record LoginResponseDto(Long id, String token, String refreshToken) {
}
