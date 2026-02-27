package com.academo.controller.dtos.group;

import jakarta.validation.constraints.NotEmpty;

public record CreateGroupDTO(
        @NotEmpty(message = "O nome é obrigatório para cadastrar um novo grupo")
        String name,
        String description
){}
