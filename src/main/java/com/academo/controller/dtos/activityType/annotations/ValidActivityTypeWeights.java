package com.academo.controller.dtos.activityType.annotations;

import com.academo.controller.dtos.activityType.validators.ValidActivityTypeWeightsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidActivityTypeWeightsValidator.class)
public @interface ValidActivityTypeWeights {

    String message() default "Pesos inválidos: cada peso deve estar entre 0 e 100, e a soma deve estar entre 0 e 100";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
