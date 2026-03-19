package com.academo.controller.dtos.payment.validators;

import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.annotations.ValidaPaymentOptionsCombination;
import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PaymentOptionsCombinationsValidator implements ConstraintValidator<ValidaPaymentOptionsCombination, PaymentLinkDTO> {

    public boolean isValid(PaymentLinkDTO paymentLinkDTO, ConstraintValidatorContext constraintValidatorContext) {
        if(paymentLinkDTO == null ||
           paymentLinkDTO.chargeType() == null ||
           paymentLinkDTO.billingType() == null) return true;

        if(paymentLinkDTO.billingType() == BillingType.BOLETO || paymentLinkDTO.billingType() == BillingType.PIX) {
            if(paymentLinkDTO.chargeType() != ChargeType.DETACHED) {
                return false;
            }
        } else { // Neste caso, a forma de pagamento é Cartão de Crédito
            if(paymentLinkDTO.chargeType() == ChargeType.DETACHED) {
                return false;
            }
            if(paymentLinkDTO.chargeType() == ChargeType.INSTALLMENT &&
                            paymentLinkDTO.maxInstallmentCount() <= 0 ||
                            paymentLinkDTO.maxInstallmentCount() > 12) {
                return false;
            }
        }
        return true;
    }
}
