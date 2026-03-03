package com.academo.controller.dtos.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserAuthDTO(
        @Email(message = "O email informado é inválido")
        @NotEmpty(message = "O email é obrigatório para a criação da conta")
        String email,
        @NotEmpty(message = "A senha é obrigatória")
        String password) {
}
