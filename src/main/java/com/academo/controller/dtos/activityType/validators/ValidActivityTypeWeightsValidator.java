package com.academo.controller.dtos.activityType.validators;

import com.academo.controller.dtos.activityType.ActivityTypeWeightDTO;
import com.academo.controller.dtos.activityType.UpdateActivityTypeWeightDTO;
import com.academo.controller.dtos.activityType.annotations.ValidActivityTypeWeights;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.List;

public class ValidActivityTypeWeightsValidator implements ConstraintValidator<ValidActivityTypeWeights, UpdateActivityTypeWeightDTO> {

    private static final BigDecimal MIN = BigDecimal.ZERO;
    private static final BigDecimal MAX = new BigDecimal("100");

    @Override
    public boolean isValid(UpdateActivityTypeWeightDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        List<ActivityTypeWeightDTO> weights = dto.weights();
        if (weights == null || weights.isEmpty()) return true;

        BigDecimal sum = BigDecimal.ZERO;

        for (ActivityTypeWeightDTO item : weights) {
            if (item == null || item.weight() == null) {
                return buildViolation(context);
            }

            BigDecimal w = item.weight();
            if (w.compareTo(MIN) < 0 || w.compareTo(MAX) > 0) {
                return buildViolation(context);
            }

            sum = sum.add(w);
        }

        boolean sumValid = sum.compareTo(MIN) >= 0 && sum.compareTo(MAX) <= 0;
        if (!sumValid) {
            return buildViolation(context);
        }

        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addConstraintViolation();
        return false;
    }
}
