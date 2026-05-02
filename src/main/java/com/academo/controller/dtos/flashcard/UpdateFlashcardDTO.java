package com.academo.controller.dtos.flashcard;

import jakarta.validation.constraints.NotEmpty;

public record UpdateFlashcardDTO(
        @NotEmpty(message = "O nível é obrigatório")
        String level,
        @NotEmpty(message = "A parte da frente do Flashcard é obrigatória")
        String frontPart,
        @NotEmpty(message = "A parte de trás do Flashcard é obrigatória")
        String backPart
) {
}
