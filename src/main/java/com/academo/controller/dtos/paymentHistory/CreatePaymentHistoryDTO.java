package com.academo.controller.dtos.paymentHistory;

import com.academo.model.enums.payment.PaymentStatus;
import com.academo.model.enums.user.PlanType;

import java.math.BigDecimal;

public record CreatePaymentHistoryDTO(
        String paymentId,
        String url,
        PaymentStatus paymentStatus,
        BigDecimal value,
        PlanType planType
) {
}
