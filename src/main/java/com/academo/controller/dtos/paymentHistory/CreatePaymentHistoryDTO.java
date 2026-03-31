package com.academo.controller.dtos.paymentHistory;

import com.academo.model.enums.PaymentStatus;
import com.academo.model.enums.PlanType;

import java.math.BigDecimal;

public record CreatePaymentHistoryDTO(
        String paymentId,
        String url,
        PaymentStatus paymentStatus,
        BigDecimal value,
        PlanType planType
) {
}
