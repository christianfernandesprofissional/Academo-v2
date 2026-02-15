package com.academo.controller.dtos.subject;

import jakarta.validation.constraints.NotEmpty;

public record CreateSubjectDTO(
        @NotEmpty(message = "O nome é obrigatório para cadastrar uma nova matéria")
        String name,
        String description
) {
}
