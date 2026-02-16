package com.academo.security.authuser;

public record LoginResponseDTO(
        String token,
        Integer userId,
        String username) {
}
