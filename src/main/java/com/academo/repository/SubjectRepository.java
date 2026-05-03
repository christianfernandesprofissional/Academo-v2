package com.academo.repository;

import com.academo.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findAllByUserId(Integer userId);
    Page<Subject> findAllByUserId(Integer userId, Pageable pageable);
    Page<Subject> findAllByUserIdAndIsActive(Integer userId, Boolean isActive, Pageable pageable);
    Optional<Subject> findByIdAndUserId(Integer subjectId, Integer userId);

    @Query(value = """
            select s
            from Subject s
            where s.user.id = :userId
              and exists (
                    select 1
                    from Flashcard f
                    where f.subject = s
                      and f.user.id = :userId
              )
            """,
            countQuery = """
            select count(s)
            from Subject s
            where s.user.id = :userId
              and exists (
                    select 1
                    from Flashcard f
                    where f.subject = s
                      and f.user.id = :userId
              )
            """)
    Page<Subject> findAllByUserIdWithFlashcards(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = """
            select s
            from Subject s
            where s.user.id = :userId
              and s.isActive = :isActive
              and exists (
                    select 1
                    from Flashcard f
                    where f.subject = s
                      and f.user.id = :userId
              )
            """,
            countQuery = """
            select count(s)
            from Subject s
            where s.user.id = :userId
              and s.isActive = :isActive
              and exists (
                    select 1
                    from Flashcard f
                    where f.subject = s
                      and f.user.id = :userId
              )
            """)
    Page<Subject> findAllByUserIdAndIsActiveWithFlashcards(@Param("userId") Integer userId,
                                                          @Param("isActive") Boolean isActive,
                                                          Pageable pageable);

    @Query("""
            select distinct s
            from Subject s
            where s.user.id = :userId
              and s.isActive = true
              and exists (
                    select 1
                    from Flashcard f
                    where f.subject = s
                      and f.user.id = :userId
              )
            """)
    List<Subject> findAllActiveByUserIdWithFlashcards(@Param("userId") Integer userId);

}
