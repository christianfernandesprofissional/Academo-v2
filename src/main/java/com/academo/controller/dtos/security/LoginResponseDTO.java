package com.academo.controller.dtos.security;

public record LoginResponseDTO(
        String token,
        Integer userId,
        String username) {
}
