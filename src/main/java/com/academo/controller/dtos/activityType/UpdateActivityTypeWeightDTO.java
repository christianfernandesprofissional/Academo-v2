package com.academo.controller.dtos.activityType;

import com.academo.controller.dtos.activityType.annotations.ValidActivityTypeWeights;
import jakarta.validation.Valid;

import java.util.List;

@ValidActivityTypeWeights
public record UpdateActivityTypeWeightDTO(
        List<@Valid ActivityTypeWeightDTO> weights
) {
}
