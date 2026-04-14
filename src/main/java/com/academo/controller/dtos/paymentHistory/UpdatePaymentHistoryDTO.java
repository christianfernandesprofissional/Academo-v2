package com.academo.controller.dtos.paymentHistory;

import com.academo.model.enums.payment.PaymentStatus;

import java.time.LocalDate;

public record UpdatePaymentHistoryDTO(
        PaymentStatus paymentStatus,
        LocalDate planDueDate
) {
}
