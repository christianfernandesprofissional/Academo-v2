package com.academo.controller.dtos.flashcard;

import com.academo.model.enums.CardLevel;

public record UpdateLevelDTO (
        Integer flashcardId,
        String level
){

}
