package com.academo.controller.dtos.activity;

import com.academo.model.Activity;
import jakarta.validation.constraints.Past;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ActivityDTO(

        Integer id,
        LocalDate activityDate,
        String name,
        BigDecimal grade,
        String description,
        String subjectName,
        String activityTypeName,
        @Past
        LocalDateTime createdAt,
        @Past
        LocalDateTime updateAt
) {

    public static ActivityDTO fromActivity(Activity activity) {
        return new ActivityDTO(activity.getId(),
                activity.getActivityDate(),
                activity.getName(),
                activity.getGrade(),
                activity.getDescription(),
                activity.getSubject().getName(),
                activity.getActivityType().getName(),
                activity.getCreatedAt(),
                activity.getUpdatedAt());
    }
}
