package com.academo.controller.dtos.period.validators;

import com.academo.controller.dtos.period.UpdateWeightDTO;
import com.academo.controller.dtos.period.annotations.WeightsSumTo100;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WeightsSumTo100Validator implements ConstraintValidator<WeightsSumTo100, UpdateWeightDTO> {

    @Override
    public boolean isValid(UpdateWeightDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        Integer first = dto.firstPeriodWeigth();
        Integer second = dto.secondPeriodWeigth();

        boolean isValid = first != null && second != null && (first + second) == 100;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
