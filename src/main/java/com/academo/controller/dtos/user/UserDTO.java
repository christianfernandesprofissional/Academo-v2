package com.academo.controller.dtos.user;

import com.academo.model.User;

import java.time.LocalDateTime;

public record UserDTO(
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long storageUsage
) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getStorageUsage()
        );
    }
}
