package com.academo.repository;

import com.academo.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    List<Group> findAllByUserId(Integer userId);
    Page<Group> findAllByUserId(Integer userId, Pageable pageable);
    Page<Group> findAllByUserIdAndIsActive(Integer userId, Boolean isActive, Pageable pageable);
    Optional<Group> findByIdAndUserId(Integer id, Integer userId);

    @Query(value = """
            select g
            from Group g
            where g.user.id = :userId
              and exists (
                    select 1
                    from Flashcard f
                    join f.subject s
                    join s.groups sg
                    where sg = g
                      and f.user.id = :userId
              )
            """,
            countQuery = """
            select count(g)
            from Group g
            where g.user.id = :userId
              and exists (
                    select 1
                    from Flashcard f
                    join f.subject s
                    join s.groups sg
                    where sg = g
                      and f.user.id = :userId
              )
            """)
    Page<Group> findAllByUserIdWithFlashcards(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = """
            select g
            from Group g
            where g.user.id = :userId
              and g.isActive = :isActive
              and exists (
                    select 1
                    from Flashcard f
                    join f.subject s
                    join s.groups sg
                    where sg = g
                      and f.user.id = :userId
              )
            """,
            countQuery = """
            select count(g)
            from Group g
            where g.user.id = :userId
              and g.isActive = :isActive
              and exists (
                    select 1
                    from Flashcard f
                    join f.subject s
                    join s.groups sg
                    where sg = g
                      and f.user.id = :userId
              )
            """)
    Page<Group> findAllByUserIdAndIsActiveWithFlashcards(@Param("userId") Integer userId,
                                                        @Param("isActive") Boolean isActive,
                                                        Pageable pageable);

}
