package com.academo.controller.dtos.flashcard;

public record UpdateFlashcardDTO(
        String level,
        String frontPart,
        String backPart
) {
}
