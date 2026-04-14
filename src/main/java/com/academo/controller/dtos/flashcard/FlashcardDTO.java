package com.academo.controller.dtos.flashcard;

import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Flashcard;
import com.academo.model.Subject;

import java.time.LocalDateTime;

public record FlashcardDTO(
        Integer id,
        Integer subjectId,
        String level,
        String frontPart,
        String backPart,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static FlashcardDTO fromFlashcard(Flashcard flashcard) {
        return new FlashcardDTO(
                flashcard.getId(),
                flashcard.getSubject().getId(),
                flashcard.getLevel().name(),
                flashcard.getFrontPart(),
                flashcard.getBackPart(),
                flashcard.getCreatedAt(),
                flashcard.getUpdatedAt()
        );
    }
}
