package com.academo.repository;

import com.academo.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Integer> {

    List<Period> findAllByUserIdAndSubjectId(Integer userId, Integer subjectId);
    Optional<Period> findByUserIdAndPeriodId(Integer userId, Integer periodId);
    void deleteByPeriodIdAndSubjectId(Integer userId, Integer periodId);

}
