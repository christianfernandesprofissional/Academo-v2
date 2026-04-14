package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.annotations.ValidaPaymentOptionsCombination;
import com.academo.model.enums.payment.BillingType;
import com.academo.model.enums.payment.ChargeType;
import com.academo.model.enums.payment.SubscriptionCycle;

@ValidaPaymentOptionsCombination
public record PaymentOptionsDTO(
        BillingType billingType,
        ChargeType chargeType,
        SubscriptionCycle subscriptionCycle
) {
}
