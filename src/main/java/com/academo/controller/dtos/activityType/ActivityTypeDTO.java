package com.academo.controller.dtos.activityType;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.model.Activity;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record ActivityTypeDTO (
        @NotEmpty(message = "O ID do tipo de atividade é obrigatório")
        Integer id,
        @NotEmpty(message = "O nome do tipo de ativadade é obrigatório")
        String name,
        String description,
        List<ActivityDTO> activities,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)
{}
