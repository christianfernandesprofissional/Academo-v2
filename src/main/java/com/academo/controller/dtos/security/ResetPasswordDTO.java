package com.academo.controller.dtos.security;

import com.academo.controller.dtos.security.annotations.PasswordsMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@PasswordsMatch
public record ResetPasswordDTO(

        @NotEmpty(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter ao menos {min} caracteres")
        String newPassword,
        @NotEmpty(message = "A confirmação de senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter ao menos {min} caracteres")
        String confirmNewPassword
) {
}
