package com.academo.controller.dtos.subject;

import com.academo.model.Subject;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public record SubjectDTO(
        Integer id,
        String name,
        String description,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static SubjectDTO fromSubject(Subject subject) {
        return new SubjectDTO(
                subject.getId(),
                subject.getName(),
                subject.getDescription(),
                subject.getIsActive(),
                subject.getCreatedAt(),
                subject.getUpdatedAt()
        );
    }
}
