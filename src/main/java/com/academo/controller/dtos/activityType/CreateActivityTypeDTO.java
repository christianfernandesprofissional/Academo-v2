package com.academo.controller.dtos.activityType;

import jakarta.validation.constraints.NotEmpty;

public record CreateActivityTypeDTO(
        @NotEmpty(message = "O nome do tipo de atividade é obrigatório")
        String name,
        String description
)
{}
