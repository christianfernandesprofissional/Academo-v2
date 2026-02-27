package com.academo.controller.dtos.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateGroupDTO(
        @NotEmpty(message = "É obrigatório o nome do grupo")
        String name,
        String description,
        @NotNull(message = "O status do grupo deve ser informado")
        Boolean isActive
) {
}
