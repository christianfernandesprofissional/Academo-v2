package com.academo.controller.dtos.security;

import com.academo.model.enums.user.UserRole;

public record LoginResponseDTO(
        String token,
        Integer userId,
        String username,
        UserRole role) {
}
