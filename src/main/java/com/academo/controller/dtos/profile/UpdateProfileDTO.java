package com.academo.controller.dtos.profile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateProfileDTO(
        String fullName,
        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate birthDate,
        @Size(max = 1, message = "Informe apenas um caractere para o gênero")
        String gender) {
}
