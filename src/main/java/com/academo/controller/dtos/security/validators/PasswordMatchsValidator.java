package com.academo.controller.dtos.security.validators;

import com.academo.controller.dtos.security.ResetPasswordDTO;
import com.academo.controller.dtos.security.annotations.PasswordsMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchsValidator implements ConstraintValidator<PasswordsMatch, ResetPasswordDTO> {

    public boolean isValid(ResetPasswordDTO dto, ConstraintValidatorContext context) {
        if(dto == null) return true;

        boolean isValid = dto.newPassword() != null && dto.newPassword().equals(dto.confirmNewPassword());

        if(!isValid) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmNewPassword")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
