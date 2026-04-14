package com.academo.controller.dtos.payment;

import com.academo.model.enums.payment.BillingType;
import com.academo.model.enums.payment.ChargeType;
import com.academo.model.enums.payment.SubscriptionCycle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record GetPaymentLinkDTO(
        String name,
        String description,
        String endDate,
        BigDecimal value,
        BillingType billingType,
        ChargeType chargeType,
        Integer dueDateLimitDays,
        SubscriptionCycle subscriptionCycle,
        Integer maxInstallmentCount,
        boolean notificationEnabled,
        //CallbackPaymentDTO callback,
        boolean isAddressRequired
) {

    static final String END_DATE = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    static final Integer DUE_DATE_LIMIT_DAYS = 10;
    static final Integer MAX_INSTALLMENT_COUNT = 1;
    static final boolean NOTIFICATION_ENABLED = false;
    static final boolean IS_ADDRESS_REQUIRED = false;

    public static GetPaymentLinkDTO fromPaymentOptions(PaymentOptionsDTO paymentOptionsDTO, BigDecimal price, CallbackPaymentDTO callback) {
        String description = paymentOptionsDTO.subscriptionCycle() == SubscriptionCycle.MONTHLY ? "Pagamento do Plano Academo Premium Mensal" : "Pagamento do Plano Academo Premium Anual ";
        return new GetPaymentLinkDTO(
                "Plano Premium",
                description,
                END_DATE,
                price,
                paymentOptionsDTO.billingType(),
                paymentOptionsDTO.chargeType(),
                DUE_DATE_LIMIT_DAYS,
                paymentOptionsDTO.subscriptionCycle(),
                MAX_INSTALLMENT_COUNT,
                NOTIFICATION_ENABLED,
                //callback,
                IS_ADDRESS_REQUIRED);
    }
}
