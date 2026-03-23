package com.academo.controller.dtos.subject;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateSubjectDTO(
        @NotEmpty(message = "O nome da matéria é obrigatório")
        String name,
        String description,
        String passingGrade,
        String calculationType,
        @NotNull(message = "É obrigatório informar o status da matéria")
        Boolean isActive
) {
}
