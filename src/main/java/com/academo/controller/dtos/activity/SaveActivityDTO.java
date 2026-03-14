package com.academo.controller.dtos.activity;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record SaveActivityDTO(

        @Future(message = "É necessário que a data da atividade seja no futuro")
        LocalDate activityDate,
        @Future(message = "É necessário que a data da notificação seja no futuro")
        LocalDate notificationDate,
        @NotEmpty(message = "É obrigatório o nome da atividade")
        String name,
        String description,
        @PositiveOrZero(message = "É obrigatório que a nota da atividade seja maior ou igual a zero")
        Double grade,
        @NotNull(message = "É necessário que a atividade pertença a uma matéria existente")
        Integer subjectId,
        @NotNull(message = "É obrigatório escolher um tipo de atividade")
        Integer activityTypeId
) {}
