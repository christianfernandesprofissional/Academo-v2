package com.academo.controller.dtos.subject;

import java.time.LocalDateTime;

public record SubjectDTO(
        Integer id,
        String name,
        String description,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
