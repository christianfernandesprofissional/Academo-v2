package com.academo.controller.dtos.activity;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ActivityDTO(

        Integer id,
        LocalDate notificationDate,
        LocalDate activityDate,
        String name,
        Double value,
        String description,
        String subjectName,
        String activityTypeName
) {}
