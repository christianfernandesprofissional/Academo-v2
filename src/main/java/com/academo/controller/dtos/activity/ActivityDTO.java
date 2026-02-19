package com.academo.controller.dtos.activity;

import com.academo.model.Activity;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ActivityDTO(

        Integer id,
        LocalDate notificationDate,
        LocalDate activityDate,
        String name,
        Double value,
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
                activity.getNotificationDate(),
                activity.getActivityDate(),
                activity.getName(),
                activity.getActivityValue(),
                activity.getDescription(),
                activity.getSubject().getName(),
                activity.getActivityType().getName(),
                activity.getCreatedAt(),
                activity.getUpdatedAt());
    }
}
