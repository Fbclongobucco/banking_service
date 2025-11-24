package com.buccodev.banking_service.controllers;

import com.buccodev.banking_service.dtos.auth.LoginRequestDto;
import com.buccodev.banking_service.dtos.auth.LoginResponseDto;
import com.buccodev.banking_service.dtos.auth.RefreshTokenRequestDto;
import com.buccodev.banking_service.security.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return authenticationService.login(loginRequestDto);
    }

    @PostMapping("/refresh-token")
    public LoginResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshToken) {
        return authenticationService.refreshToken(refreshToken);
    }
}
