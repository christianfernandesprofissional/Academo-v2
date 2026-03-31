package com.academo.controller.dtos.paymentHistory;

import com.academo.model.PaymentHistory;
import com.academo.model.enums.PaymentStatus;

import java.math.BigDecimal;

public record UpdatePaymentHistoryDTO(
        PaymentStatus paymentStatus
) {
}
