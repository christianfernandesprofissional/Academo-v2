package com.academo.controller.dtos.payment;

import com.academo.model.enums.payment.BillingType;
import com.academo.model.enums.payment.ChargeType;
import com.academo.model.enums.payment.SubscriptionCycle;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentLinkDTO(
        String id,
        String name,
        BigDecimal value,
        boolean active,
        ChargeType chargeType,
        String url,
        BillingType billingType,
        SubscriptionCycle subscriptionCycle,
        String description,
        LocalDate date,
        boolean deleted,
        Integer viewCount,
        Integer maxInstallmentCount,
        Integer dueDateLimitDays,
        boolean notificationEnabled,
        boolean isAddressRequired,
        String externalReference
) {
}
