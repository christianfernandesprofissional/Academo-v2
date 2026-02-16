package com.academo.controller.dtos.activity;
import java.time.LocalDate;
import java.util.Date;

public record ActivityNotificationDTO(

        //Ver em que contexto está sendo utilizado
        String name,
        String description,
        String subject,
        LocalDate activityDate,
        LocalDate notificationDate
) {}