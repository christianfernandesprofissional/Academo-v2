package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;

import java.time.LocalDate;

public record PaymentLinkDTO(
        String id,
        String name,
        Double value,
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
