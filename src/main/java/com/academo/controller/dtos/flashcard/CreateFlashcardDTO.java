package com.academo.controller.dtos.flashcard;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateFlashcardDTO(
        @NotNull(message = "O ID da matéria é obrigatório")
        Integer subjectId,
        @NotEmpty(message = "A parte da frente do Flashcard é obrigatória")
        String frontPart,
        @NotEmpty(message = "A parte de trás do Flashcard é obrigatória")
        String backPart
) {
}
