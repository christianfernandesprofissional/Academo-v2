package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;

public record PaymentOptionsData(
        BillingType billingType,
        ChargeType chargeType,
        SubscriptionCycle subscriptionCycle
) {
}
