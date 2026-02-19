package com.academo.repository;

import com.academo.model.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Integer> {
    Boolean existsByNameAndUserId(String name, Integer userId);
    List<ActivityType> findAll(Integer userId);
    Optional<ActivityType> findById(Integer activityTypeId, Integer userId);
}
