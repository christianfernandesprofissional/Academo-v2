package com.academo.controller.dtos.payment.annotations;


import com.academo.controller.dtos.payment.validators.PaymentOptionsCombinationsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentOptionsCombinationsValidator.class)
public @interface ValidaPaymentOptionsCombination {

    String message() default "Combinação inválida de opções de pagamento";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
