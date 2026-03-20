package com.academo.controller.dtos.payment.validators;

import com.academo.controller.dtos.payment.GetPaymentLinkDTO;
import com.academo.controller.dtos.payment.annotations.ValidaPaymentOptionsCombination;
import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PaymentOptionsCombinationsValidator implements ConstraintValidator<ValidaPaymentOptionsCombination, GetPaymentLinkDTO> {

    public boolean isValid(GetPaymentLinkDTO getPaymentLinkDTO, ConstraintValidatorContext constraintValidatorContext) {
        if(getPaymentLinkDTO == null ||
           getPaymentLinkDTO.chargeType() == null ||
           getPaymentLinkDTO.billingType() == null) return true;

        if(getPaymentLinkDTO.billingType() == BillingType.BOLETO || getPaymentLinkDTO.billingType() == BillingType.PIX) {
            if(getPaymentLinkDTO.chargeType() != ChargeType.DETACHED) {
                return false;
            }
        } else { // Neste caso, a forma de pagamento é Cartão de Crédito
            if(getPaymentLinkDTO.chargeType() == ChargeType.DETACHED) {
                return false;
            }
            if(getPaymentLinkDTO.chargeType() == ChargeType.INSTALLMENT &&
                            getPaymentLinkDTO.maxInstallmentCount() <= 0 ||
                            getPaymentLinkDTO.maxInstallmentCount() > 12) {
                return false;
            }
        }
        return true;
    }
}
