package com.academo.repository;

import com.academo.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Integer> {

    List<Period> findAllByUserIdAndSubjectId(Integer userId, Integer subjectId);
    Page<Period> findAllByUserIdAndSubjectId(Integer userId, Integer subjectId, Pageable pageable);
    Optional<Period> findByIdAndUserId(Integer periodId, Integer userId);
    void deleteByIdAndSubjectIdAndUserId(Integer periodId, Integer subjectId, Integer userId);

}
