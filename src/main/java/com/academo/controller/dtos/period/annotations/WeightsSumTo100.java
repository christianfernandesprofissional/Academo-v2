package com.academo.controller.dtos.period.annotations;

import com.academo.controller.dtos.period.validators.WeightsSumTo100Validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WeightsSumTo100Validator.class)
public @interface WeightsSumTo100 {

    String message() default "A soma dos pesos deve ser exatamente 100%";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
