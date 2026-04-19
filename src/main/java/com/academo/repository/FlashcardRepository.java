package com.academo.repository;

import com.academo.model.Flashcard;
import com.academo.model.enums.flashcard.CardLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

    List<Flashcard> findAllByUserIdAndSubjectId(Integer userId, Integer subjectId);
    List<Flashcard> findAllByUserId(Integer userId);
    Page<Flashcard> findAllByUserId(Integer userId, Pageable pageable);
    Optional<Flashcard> findByIdAndUserId(Integer id, Integer userId);

    @Query("""
            select distinct f
            from Flashcard f
            join f.subject s
            join s.groups g
            where f.user.id = :userId
              and g.id = :groupId
              and (:level is null or f.level = :level)
            """)
    List<Flashcard> findAllByUserIdAndGroupIdAndLevel(@Param("userId") Integer userId,
                                                     @Param("groupId") Integer groupId,
                                                     @Param("level") CardLevel level);
}
