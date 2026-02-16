package com.academo.security.service;

import com.academo.security.authuser.AuthUser;
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

    @Value("${api.security.token.secret}")
    private String secret;

    private final String activationSecret = "activationKey";

    private static final Instant ACTIVATION_TOKEN = LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    private static final Instant ACCESS_TOKEN = LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));

    // ---------------- LOGIN TOKEN -----------------------
    public String generateLoginToken(AuthUser user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("academo")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generationExpirationDate(false))
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
                    .withExpiresAt(generationExpirationDate(true))
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

    private Instant generationExpirationDate(boolean isActivationToken) {
        if(isActivationToken) {
            return ACTIVATION_TOKEN;
        }
        return ACCESS_TOKEN;
    }


}
