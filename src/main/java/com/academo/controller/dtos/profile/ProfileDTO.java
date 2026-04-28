package com.academo.controller.dtos.profile;


import com.academo.model.Profile;
import com.academo.model.enums.user.PlanType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileDTO(
        String fullName,
        LocalDate birthDate,
        Character gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        PlanType planType,
        Long userUseStorage
) {

    public static ProfileDTO fromProfile(Profile profile, Long userUseStorage, PlanType planType) {
        return new ProfileDTO(
                profile.getFullName(),
                profile.getBirthDate(),
                profile.getGender(),
                profile.getCreatedAt(),
                profile.getUpdatedAt(),
                planType,
                userUseStorage
        );
    }
}


