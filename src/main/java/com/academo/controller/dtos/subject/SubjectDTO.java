package com.academo.controller.dtos.subject;

import com.academo.model.Subject;

import java.time.LocalDateTime;

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

    public static Subject toSubject(Integer subjectId, SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectDTO.name());
        subject.setDescription(subjectDTO.description());
        subject.setIsActive(subjectDTO.isActive());
        subject.setCreatedAt(subjectDTO.createdAt());
        subject.setUpdatedAt(subjectDTO.updatedAt());
        return subject;
    }
}
