package com.academo.controller.dtos.activityType;

import jakarta.validation.constraints.NotEmpty;

public record SaveActivityTypeDTO(
        @NotEmpty(message = "O nome do tipo de atividade é obrigatório")
        String name,
        String description,
        Integer periodId
)
{}
