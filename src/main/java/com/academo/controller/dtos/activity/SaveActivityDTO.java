package com.academo.controller.dtos.activity;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaveActivityDTO(

        LocalDate activityDate,
        @NotEmpty(message = "É obrigatório o nome da atividade")
        String name,
        String description,
        @NotNull(message = "É obrigatório informar uma nota para atividade")
        @PositiveOrZero(message = "É obrigatório que a nota da atividade seja maior ou igual a zero")
        @DecimalMax(value = "10" ,message = "A nota máxima para uma atividade é 10")
        @Digits(integer = 2, fraction = 2, message = "Só é permitido 2 casas decimais")
        BigDecimal grade,
        @NotNull(message = "É necessário que a atividade pertença a uma matéria existente")
        Integer subjectId,
        @NotNull(message = "É obrigatório escolher um tipo de atividade")
        Integer activityTypeId
) {}
