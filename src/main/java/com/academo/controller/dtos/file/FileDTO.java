package com.academo.controller.dtos.file;

import com.academo.model.File;

import java.time.LocalDateTime;

public record FileDTO(
        String uuid,
        String fileName,
        String path,
        String fileType,
        Long size,
        Integer subjectId,
        LocalDateTime createdAt
) {

    public static FileDTO fromFile(File file) {
        return new FileDTO(
                file.getUuid(),
                file.getFileName(),
                file.getPath(),
                file.getFileType(),
                file.getSize(),
                file.getSubject().getId(),
                file.getCreatedAt()
        );
    }
}
