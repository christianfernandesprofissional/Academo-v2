package com.academo.controller.dtos.subject;

import jakarta.validation.constraints.NotEmpty;

public record UpdateSubjectDTO(
        @NotEmpty(message = "O nome da matéria é obrigatório")
        String name,
        String description,
        @NotEmpty(message = "É obrigatório informar o status da matéria")
        Boolean isActive
) {
}
