package com.academo.controller.dtos.user;

import com.academo.model.User;
import com.academo.model.enums.user.PlanType;

import java.time.LocalDateTime;

public record UserDTO(
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        PlanType planType,
        Long storageUsage
) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getPlanType(),
                user.getStorageUsage()
        );
    }
}
