package com.academo.service.flashcard;

import com.academo.controller.dtos.flashcard.CreateFlashcardDTO;
import com.academo.controller.dtos.flashcard.FlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateFlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateLevelDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IFlashcardService {

    Page<FlashcardDTO> findAllBySubjectId(Integer userId, Integer subjectId, Pageable pageable);
    Page<FlashcardDTO> findAllByUserId(Integer userId, Pageable pageable);
    Page<FlashcardDTO> findAllByLevel(Integer userId, Integer subjectId, String level, Pageable pageable);
    Page<FlashcardDTO> findAllByGroupId(Integer userId, Integer groupId, String level, Pageable pageable);
    FlashcardDTO findById(Integer userId, Integer flashcardId);
    FlashcardDTO create(Integer userId, CreateFlashcardDTO dto);
    FlashcardDTO update(Integer userId, Integer flashcardId, UpdateFlashcardDTO dto);
    FlashcardDTO updateLevel(Integer userId, Integer flashcardId, UpdateLevelDTO dto);
    void delete(Integer userId, Integer flashcardId);
}
