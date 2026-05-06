package com.academo.controller.dtos.activityType;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ActivityTypeWeightDTO(
    @NotNull(message = "É obrigatório o ID de Tipo de Atividade")
    Integer activityTypeId,
    @Min(value = 0, message = "O valor mínimo para o peso é 0")
    @Max(value = 100, message = "O valor máximo para o peso é 100")
    BigDecimal weight
) {
    
}