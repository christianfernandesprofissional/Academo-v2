package com.academo.controller.dtos.group;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AssociateSubjectsDTO(
        @NotEmpty(message = "É obrigatório ao menos uma matéria")
        List<Integer> subjectsIds
) {
}
