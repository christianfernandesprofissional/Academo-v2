package com.academo.controller.dtos.group;

import com.academo.controller.GroupController;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Group;
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
){
    public static GroupDTO fromGroup(Group g){
        return new GroupDTO(
                        g.getId(),
                        g.getName(),
                        g.getDescription(),
                        g.getIsActive(),
                        g.getCreatedAt(),
                        g.getUpdatedAt(),
                        g.getSubjects().stream() //A lista de Subject do grupo é transformada em uma lista de SubjectDTO
                                .map(s -> new SubjectDTO(
                                        s.getId(),
                                        s.getName(),
                                        s.getDescription(),
                                        s.getIsActive(),
                                        s.getCreatedAt(),
                                        s.getUpdatedAt()
                                )).toList()
                );
    }

}
