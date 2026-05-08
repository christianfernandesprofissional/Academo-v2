package com.academo.controller.dtos.activityType;

import jakarta.validation.constraints.*;


public record UpdateActivityTypeDTO (
        @NotEmpty(message = "O nome do tipo de ativadade é obrigatório")
        String name,
        String description,
        Integer periodId
){

}
