package com.academo.controller.dtos.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(
        @Email(message = "O formato do email está incorreto")
        @NotBlank(message = "É obrigatório informar o email")
        String email
) {
}
