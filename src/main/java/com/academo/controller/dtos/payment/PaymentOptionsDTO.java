package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.annotations.ValidaPaymentOptionsCombination;
import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;

@ValidaPaymentOptionsCombination
public record PaymentOptionsDTO(
        BillingType billingType,
        ChargeType chargeType,
        SubscriptionCycle subscriptionCycle
) {
}
