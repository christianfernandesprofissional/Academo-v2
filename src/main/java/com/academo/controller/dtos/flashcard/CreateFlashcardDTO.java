package com.academo.controller.dtos.flashcard;

public record CreateFlashcardDTO(
        Integer subjectId,
        String frontPart,
        String backPart
) {
}
