package com.academo.controller.dtos.paymentHistory;

import com.academo.model.PaymentHistory;
import com.academo.model.enums.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentHistoryDTO(
        Integer paymentHistoryId,
        String paymentId,
        PaymentStatus paymentStatus,
        BigDecimal value,
        LocalDate planDueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static PaymentHistoryDTO fromPaymentHistory(PaymentHistory paymentHistory) {
        return new PaymentHistoryDTO(
                paymentHistory.getId(),
                paymentHistory.getPaymentId(),
                paymentHistory.getStatus(),
                paymentHistory.getValue(),
                paymentHistory.getPlanDueDate(),
                paymentHistory.getCreatedAt(),
                paymentHistory.getUpdatedAt());
    }
}
