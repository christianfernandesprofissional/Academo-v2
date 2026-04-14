package com.academo.controller.dtos.period;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdatePeriodDTO(
        Integer subjectId,
        String name,
        @PositiveOrZero(message = "É obrigatório que a nota da atividade seja maior ou igual a zero")
        @DecimalMax(value = "10" ,message = "A nota máxima para uma atividade é 10")
        @Digits(integer = 2, fraction = 2, message = "Só é permitido 2 casas decimais")
        BigDecimal grade,
        @Min(value = 1, message = "O valor mínimo de peso é 1%")
        @Max(value = 100, message = "O valor máximo de peso e 100%")
        Integer weight
        ) {

}
