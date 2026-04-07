package com.academo.service.flashcard;

import com.academo.controller.dtos.flashcard.CreateFlashcardDTO;
import com.academo.controller.dtos.flashcard.FlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateFlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateLevelDTO;
import com.academo.model.enums.CardLevel;

import java.util.List;

public interface IFlashcardService {

    List<FlashcardDTO> findAllBySubjectId(Integer userId, Integer subjectId);
    List<FlashcardDTO> findAllByUserId(Integer userId);
    List<FlashcardDTO> findAllByLevel(Integer userId, Integer subjectId, String level);
    FlashcardDTO findById(Integer userId, Integer flashcardId);
    FlashcardDTO create(Integer userId, CreateFlashcardDTO dto);
    FlashcardDTO update(Integer userId, UpdateFlashcardDTO dto);
    FlashcardDTO updateLevel(Integer userId, UpdateLevelDTO dto);
    void delete(Integer userId, Integer flashcardId);
}
