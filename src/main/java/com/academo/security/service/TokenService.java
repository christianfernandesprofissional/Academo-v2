package com.academo.security.service;

import com.academo.security.authuser.AuthUser;
import com.academo.security.enums.TokenExpirationType;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.jwt.secret}")
    private String secret;
    @Value("${api.security.token.activation.secret}")
    private String activationSecret;
    @Value("${api.security.token.reset-password.secret}")
    private String resetPasswordSecret;

    // ---------------- LOGIN TOKEN -----------------------
    public String generateLoginToken(AuthUser user) {
        System.out.println(generateExpiration(TokenExpirationType.ACCESS_TOKEN));
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("academo")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpiration(TokenExpirationType.ACCESS_TOKEN))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token",e);
        }
    }

    public String validateLoginToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("academo")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }


    // ---------------- ACCOUNT ACTIVATION TOKEN -----------------------
    public String generateActivationToken(Integer userId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(activationSecret);
            String token = JWT.create()
                    .withIssuer("academo")
                    .withSubject(String.valueOf(userId))
                    .withExpiresAt(generateExpiration(TokenExpirationType.ACTIVATION_TOKEN))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token de ativação!",e);
        }
    }

    public String validateActivationToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(activationSecret);
            return JWT.require(algorithm)
                    .withIssuer("academo")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String generateForgotPasswordToken(Integer userId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(resetPasswordSecret);
            return JWT.create()
                    .withIssuer("academo")
                    .withSubject(String.valueOf(userId))
                    .withExpiresAt(generateExpiration(TokenExpirationType.RESET_PASSWORD_TOKEN))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            return null;
        }
    }

    public String validateForgotPasswordToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(resetPasswordSecret);
            return JWT.require(algorithm)
                    .withIssuer("academo")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant generateExpiration(TokenExpirationType expirationType) {
        switch (expirationType) {
            case ACCESS_TOKEN -> {
                return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
            }
            case ACTIVATION_TOKEN -> {
                return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
            }
            case RESET_PASSWORD_TOKEN -> {
                return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00"));
            }
            default -> {
                throw new RuntimeException("Erro ao gerar prazo de expiração do Token");
            }
        }
    }


}
