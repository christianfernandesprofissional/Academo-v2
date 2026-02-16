package com.academo.controller.dtos.profile;

import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateProfileDTO(
        String fullName,
        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate birthDate,
        Character gender,
        String institution) {
}
