package com.academo.controller.dtos.flashcard;

public record UpdateFlashcardDTO(
        Integer id,
        String level,
        String frontPart,
        String backPart
) {
}
