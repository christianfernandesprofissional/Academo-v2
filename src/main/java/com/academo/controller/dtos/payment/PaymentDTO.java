package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.enums.BillingType;

import java.time.LocalDate;

public record PaymentDTO(
        String customer,
        BillingType billingType,
        Double value,
        LocalDate dueDate,
        String description,
        Integer daysAfterDueDateToRegistrationCancellation,
        Integer installmentCount,
        Double totalValue,
        Double installmentValue,
        CallbackPaymentDTO callback,
        String pixAutomaticAuthorizationId
) {
}
