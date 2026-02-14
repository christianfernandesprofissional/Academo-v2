package com.academo.controller.dtos.profile;

import java.time.LocalDate;

public record UpdateProfileDTO(
        String fullName,
        LocalDate birthDate,
        Character gender,
        String institution) {
}
