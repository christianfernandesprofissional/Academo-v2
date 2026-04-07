package com.academo.repository;

import com.academo.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

    List<Flashcard> findAllByUserIdAndSubjectId(Integer userId, Integer subjectId);
    List<Flashcard> findAllByUserId(Integer userId);
    Optional<Flashcard> findByIdAndUserId(Integer id, Integer userId);
}
