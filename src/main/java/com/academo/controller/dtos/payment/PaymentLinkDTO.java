package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;

import java.time.LocalDate;

public record PaymentLinkDTO(

        // Enviados pelo Cliente
        BillingType billingType,
        ChargeType chargeType,
        SubscriptionCycle subscriptionCycle,


        // Geridos pelo ACADEMO
        String name,
        String description,
        LocalDate endDate,
        Double value,





        Integer dueDateLimitDays,
        Integer maxInstallmentCount,
        boolean notificationEnabled, // = false
        CallbackPaymentDTO callback,
        boolean isAddressRequired // = false
) {
}
