package com.academo.repository;

import com.academo.model.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Integer> {
    Boolean existsByNameAndUserIdAndPeriodId(String name, Integer userId, Integer periodId);
    List<ActivityType> findAllByPeriodIdAndUserId(Integer periodId, Integer userId);
    Page<ActivityType> findAllByPeriodIdAndUserId(Integer periodId, Integer userId, Pageable pageable);
    Optional<ActivityType> findByIdAndUserId(Integer activityTypeId, Integer userId);
}
