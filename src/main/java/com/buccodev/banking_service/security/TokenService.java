package com.buccodev.banking_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.buccodev.banking_service.exceptions.auth.InvalidTokenException;
import com.buccodev.banking_service.exceptions.auth.JWTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TokenService {

    @Value("${token.secret}")
    private String secret;

    public String generateToken(UserDetails user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("banking-service")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationTokenDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTException("Error while generating token");
        }
    }

    public String generateRefreshToken(UserDetails user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(genRefreshExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTException("Error while generating refresh token");
        }
    }

    public String validateRefreshToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new InvalidTokenException("token not found, expired or invalid");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("banking-service")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new InvalidTokenException("token not found, expired or invalid");
        }
    }

    private Instant genExpirationTokenDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

    private Instant genRefreshExpirationDate() {
        return LocalDateTime.now()
                .plusDays(1)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }
}
