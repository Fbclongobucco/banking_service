package com.buccodev.banking_service.security;

import com.buccodev.banking_service.dtos.auth.LoginRequestDto;
import com.buccodev.banking_service.dtos.auth.LoginResponseDto;
import com.buccodev.banking_service.dtos.auth.RefreshTokenRequestDto;
import com.buccodev.banking_service.dtos.auth.UserLoggedDto;
import com.buccodev.banking_service.entities.Customer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final FindUserForAuthentication findUserForAuthentication;

    public AuthenticationService(AuthenticationManager authenticationManager, TokenService tokenService, FindUserForAuthentication findUserForAuthentication) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.findUserForAuthentication = findUserForAuthentication;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password());

        var authentication = authenticationManager.authenticate(usernamePassword);
        var user = (UserDetails) authentication.getPrincipal();
        var token = tokenService.generateToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);
        var userAuthenticated = findUserForAuthentication.findUserForAuthentication(loginRequestDto.email());

        var userLogged = (Customer) userAuthenticated;

        return new LoginResponseDto(
                new UserLoggedDto(userLogged.getId(), userLogged.getName(), userLogged.getEmail(), userLogged.getRole().toString()),
                token,
                refreshToken
        );
    }

    public LoginResponseDto refreshToken(RefreshTokenRequestDto refreshTokenDto) {
        var user = tokenService.validateRefreshToken(refreshTokenDto.refreshToken());
        var userAuthenticated = findUserForAuthentication.findUserForAuthentication(user);
        var token = tokenService.generateToken(userAuthenticated);
        var newRefreshToken = tokenService.generateRefreshToken(userAuthenticated);

        var userLogged = (Customer) userAuthenticated;

        return new LoginResponseDto(
                new UserLoggedDto(userLogged.getId(), userLogged.getName(), userLogged.getEmail(), userLogged.getRole().toString()),
                token,
                newRefreshToken
        );
    }


}
