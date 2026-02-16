package com.academo.controller.dtos.group;

import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Subject;

import java.time.LocalDateTime;
import java.util.List;

public record GroupDTO (
        Integer id,
        String name,
        String description,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<SubjectDTO> subjects
){}
