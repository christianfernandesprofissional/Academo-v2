package com.academo.controller.dtos.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AssociateSubjectsDTO(
        @Size(min=1, message = "É obrigatório ao menos uma matéria")
        List<Integer> subjectsIds
) {
}
