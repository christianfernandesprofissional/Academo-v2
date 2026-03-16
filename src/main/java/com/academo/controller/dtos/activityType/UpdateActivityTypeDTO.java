package com.academo.controller.dtos.activityType;

import com.academo.controller.dtos.activity.ActivityDTO;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateActivityTypeDTO (
        @NotEmpty(message = "O nome do tipo de ativadade é obrigatório")
        String name,
        String description,
        String weight,
        Integer periodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){

}
