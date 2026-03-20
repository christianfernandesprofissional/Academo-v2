package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public record GetPaymentLinkDTO(
        String name,
        String description,
        LocalDate endDate,
        Double value,
        BillingType billingType,
        ChargeType chargeType,
        Integer dueDateLimitDays,
        SubscriptionCycle subscriptionCycle,
        Integer maxInstallmentCount,
        boolean notificationEnabled,
        CallbackPaymentDTO callback,
        boolean isAddressRequired
) {

    static final LocalDate END_DATE = LocalDate.now(ZoneId.of("America/Sao_Paulo")).plusDays(2);
    public static GetPaymentLinkDTO fromPaymentOptions(PaymentOptionsData paymentOptionsData, Double price, CallbackPaymentDTO callback) {
        return new GetPaymentLinkDTO(
                "Pagamento do Plano",
                "Link de Pagamento do Plano Premium",
                END_DATE,
                price,
                paymentOptionsData.billingType(),
                paymentOptionsData.chargeType(),
                10,
                paymentOptionsData.subscriptionCycle(),
                12,
                false,
                callback,
                false);
    }
}
