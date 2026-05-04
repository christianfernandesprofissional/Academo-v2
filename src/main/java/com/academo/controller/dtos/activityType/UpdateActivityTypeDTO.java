package com.academo.controller.dtos.activityType;

import com.academo.controller.dtos.activity.ActivityDTO;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record UpdateActivityTypeDTO (
        @NotEmpty(message = "O nome do tipo de ativadade é obrigatório")
        String name,
        String description,
        Integer periodId
){

}
