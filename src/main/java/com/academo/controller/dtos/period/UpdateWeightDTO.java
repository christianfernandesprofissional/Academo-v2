package com.academo.controller.dtos.period;

import com.academo.controller.dtos.period.annotations.WeightsSumTo100;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@WeightsSumTo100
public record UpdateWeightDTO(
        @Min(value = 1, message = "O valor mínimo de peso é 1%")
        @Max(value = 100, message = "O valor máximo de peso e 100%")
        Integer firstPeriodWeight,
        @Min(value = 1, message = "O valor mínimo de peso é 1%")
        @Max(value = 100, message = "O valor máximo de peso e 100%")
        Integer secondPeriodWeight
) {
}
