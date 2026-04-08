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
        @Min(value = 1, message = "O valor mínimo de peso é 1%")
        @Max(value = 100, message = "O valor máximo de peso e 100%")
        Integer weight,
        Integer periodId
){

}
