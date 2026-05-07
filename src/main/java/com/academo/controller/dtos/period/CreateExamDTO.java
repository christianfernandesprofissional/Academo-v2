package com.academo.controller.dtos.period;

import jakarta.validation.constraints.*;

public record CreateExamDTO(
        @NotNull(message = "O período deve ter uma matéria")
        Integer subjectId
) {
}
