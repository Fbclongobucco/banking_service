package com.buccodev.banking_service.dtos.auth;

public record LoginResponseDto(UserLoggedDto userLogged, String token, String refreshToken) {
}
