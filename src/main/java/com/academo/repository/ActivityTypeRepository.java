package com.academo.repository;

import com.academo.model.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Integer> {
    Boolean existsByNameAndUserIdAndPeriodId(String name, Integer userId, Integer periodId);
    List<ActivityType> findAllByPeriodIdAndUserId(Integer periodId, Integer userId);
    Optional<ActivityType> findByIdAndUserId(Integer activityTypeId, Integer userId);
}
