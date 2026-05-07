package com.academo.controller.dtos.group;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AssociateSubjectsDTO(
        @NotNull(message = "É obrigatório o envio de ao menos uma matéria")
        @Size(min=1, message = "É obrigatório ao menos uma matéria")
        List<Integer> subjectsIds
) {
}
