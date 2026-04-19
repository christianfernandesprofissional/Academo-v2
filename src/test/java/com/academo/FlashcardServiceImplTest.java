package com.academo;

import com.academo.controller.dtos.flashcard.CreateFlashcardDTO;
import com.academo.controller.dtos.flashcard.FlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateFlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateLevelDTO;
import com.academo.model.Flashcard;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.model.enums.flashcard.CardLevel;
import com.academo.repository.FlashcardRepository;
import com.academo.service.flashcard.FlashcardServiceImpl;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.flashcard.FlashcardNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceImplTest {

    @Mock
    private FlashcardRepository repository;

    @InjectMocks
    private FlashcardServiceImpl service;

    private Flashcard flashcard;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setId(1);

        Subject subject = new Subject();
        subject.setId(1);

        flashcard = new Flashcard();
        flashcard.setId(10);
        flashcard.setFrontPart("front");
        flashcard.setBackPart("back");
        flashcard.setLevel(CardLevel.SEM_NIVEL);
        flashcard.setUser(user);
        flashcard.setSubject(subject);
    }

    @Test
    void shouldFindAllBySubjectId() {
        when(repository.findAllByUserIdAndSubjectId(1, 1)).thenReturn(List.of(flashcard));

        List<FlashcardDTO> result = service.findAllBySubjectId(1, 1);

        assertEquals(1, result.size());
    }

    @Test
    void shouldFindAllByLevel() {
        when(repository.findAllByUserIdAndSubjectId(1, 1)).thenReturn(List.of(flashcard));

        List<FlashcardDTO> result = service.findAllByLevel(1, 1, "sem_nivel");

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowWhenFindAllByLevelInvalid() {
        assertThrows(FlashcardNotFoundException.class, () -> service.findAllByLevel(1, 1, "INVALID"));
    }

    @Test
    void shouldFindById() {
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(flashcard));

        FlashcardDTO dto = service.findById(1, 10);

        assertNotNull(dto);
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.empty());

        assertThrows(FlashcardNotFoundException.class, () -> service.findById(1, 10));
    }

    @Test
    void shouldCreateFlashcard() {
        CreateFlashcardDTO dto = new CreateFlashcardDTO(1, "front", "back");

        when(repository.save(any())).thenAnswer(inv -> {
            Flashcard f = inv.getArgument(0);
            f.setId(10);
            return f;
        });

        FlashcardDTO created = service.create(1, dto);

        assertNotNull(created);
        verify(repository).save(any(Flashcard.class));
    }

    @Test
    void shouldUpdateFlashcard() {
        UpdateFlashcardDTO dto = new UpdateFlashcardDTO("SEM_NIVEL", "f2", "b2");

        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(flashcard));
        when(repository.save(any())).thenReturn(flashcard);

        FlashcardDTO updated = service.update(1, 10, dto);

        assertNotNull(updated);
        verify(repository).save(flashcard);
    }

    @Test
    void shouldThrowWhenUpdatingWithInvalidLevel() {
        UpdateFlashcardDTO dto = new UpdateFlashcardDTO("INVALID", "f2", "b2");

        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(flashcard));

        assertThrows(FlashcardNotFoundException.class, () -> service.update(1, 10, dto));

        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateLevel() {
        UpdateLevelDTO dto = new UpdateLevelDTO("SEM_NIVEL");

        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(flashcard));
        when(repository.save(any())).thenReturn(flashcard);

        FlashcardDTO updated = service.updateLevel(1, 10, dto);

        assertNotNull(updated);
        verify(repository).save(flashcard);
    }

    @Test
    void shouldDeleteFlashcard() {
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(flashcard));

        service.delete(1, 10);

        verify(repository).deleteById(10);
    }

    @Test
    void shouldThrowWhenDeletingOtherUser() {
        User other = new User();
        other.setId(2);
        flashcard.setUser(other);

        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(flashcard));

        assertThrows(NotAllowedInsertionException.class, () -> service.delete(1, 10));

        verify(repository, never()).deleteById(any());
    }
}
