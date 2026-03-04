package com.academo.controller.dtos.activityType;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record ActivityTypeDTO (
        @NotEmpty(message = "O ID do tipo de atividade é obrigatório")
        Integer id,
        @NotEmpty(message = "O nome do tipo de ativadade é obrigatório")
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)
{}
