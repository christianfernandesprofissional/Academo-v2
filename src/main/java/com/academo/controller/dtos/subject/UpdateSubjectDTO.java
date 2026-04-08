package com.academo.controller.dtos.subject;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateSubjectDTO(
        @NotEmpty(message = "O nome da matéria é obrigatório")
        String name,
        String description,
        @PositiveOrZero(message = "É obrigatório que a nota da atividade seja maior ou igual a zero")
        @DecimalMax(value = "10" ,message = "A nota máxima para uma matéria é 10")
        @Digits(integer = 2, fraction = 2, message = "Só é permitido 2 casas decimais")
        BigDecimal passingGrade,
        String calculationType,
        @NotNull(message = "É obrigatório informar o status da matéria")
        Boolean isActive
) {
}
