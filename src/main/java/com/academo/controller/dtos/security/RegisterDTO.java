package com.academo.controller.dtos.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotEmpty(message = "O nome é obrigatório para a criação da conta")
        String name,
        @NotEmpty(message = "A senha é obrigatória para a criação da conta")
        @Size(min = 8, message = "A senha deve ter ao menos {value} caracteres")
        String password,
        @NotEmpty(message = "O email é obrigatório para a criação da conta")
        @Email(message = "O email informado é inválido")
        String email) {
}
