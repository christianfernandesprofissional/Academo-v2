package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;

import java.time.LocalDate;
import java.time.ZoneId;

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
    static final Integer DUE_DATE_LIMIT_DAYS = 10;
    static final Integer MAX_INSTALLMENT_COUNT = 12;
    static final boolean NOTIFICATION_ENABLED = false;
    static final boolean IS_ADDRESS_REQUIRED = false;

    public static GetPaymentLinkDTO fromPaymentOptions(PaymentOptionsDTO paymentOptionsDTO, Double price, CallbackPaymentDTO callback) {
        return new GetPaymentLinkDTO(
                "Pagamento do Plano",
                "Link de Pagamento do Plano Premium",
                END_DATE,
                price,
                paymentOptionsDTO.billingType(),
                paymentOptionsDTO.chargeType(),
                DUE_DATE_LIMIT_DAYS,
                paymentOptionsDTO.subscriptionCycle(),
                MAX_INSTALLMENT_COUNT,
                NOTIFICATION_ENABLED,
                callback,
                IS_ADDRESS_REQUIRED);
    }
}
