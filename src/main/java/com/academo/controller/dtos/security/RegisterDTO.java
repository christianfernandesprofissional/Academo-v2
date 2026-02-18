package com.academo.controller.dtos.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterDTO(
        @NotEmpty(message = "O nome é obrigatório para a criação da conta")
        String name,
        @NotEmpty(message = "A senha é obrigatória para a criação da conta")
        String password,
        @NotEmpty(message = "O email é obrigatório para a criação da conta")
        @Email(message = "O email informado é inválido")
        String email) {
}
