package com.academo.controller.dtos.payment;

import com.academo.model.enums.PlanType;

import java.math.BigDecimal;

public record PlanTypeDataDTO(
        PlanType planType,
        BigDecimal value
) {
}
