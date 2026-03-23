package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
       // CallbackPaymentDTO callback,
        boolean isAddressRequired
) {

    static final String END_DATE = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    static final Integer DUE_DATE_LIMIT_DAYS = 10;
    static final Integer MAX_INSTALLMENT_COUNT = 12;
    static final boolean NOTIFICATION_ENABLED = false;
    static final boolean IS_ADDRESS_REQUIRED = false;

    public static GetPaymentLinkDTO fromPaymentOptions(PaymentOptionsDTO paymentOptionsDTO, BigDecimal price, CallbackPaymentDTO callback) {
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
                //callback,
                IS_ADDRESS_REQUIRED);
    }
}
