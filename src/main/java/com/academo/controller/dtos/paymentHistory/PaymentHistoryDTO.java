package com.academo.controller.dtos.paymentHistory;

import com.academo.model.PaymentHistory;
import com.academo.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentHistoryDTO(
        String paymentId,
        PaymentStatus paymentStatus,
        BigDecimal value,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static PaymentHistoryDTO fromPaymentHistory(PaymentHistory paymentHistory) {
        return new PaymentHistoryDTO(
                paymentHistory.getPaymentId(),
                paymentHistory.getStatus(),
                paymentHistory.getValue(),
                paymentHistory.getCreatedAt(),
                paymentHistory.getUpdatedAt());
    }
}
