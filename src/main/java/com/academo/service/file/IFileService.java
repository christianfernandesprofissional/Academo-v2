package com.academo.service.file;

import com.academo.controller.dtos.file.FileDTO;
import com.academo.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {

    FileDTO createFile(MultipartFile file, Integer userId, Integer subjectId);
    File findFileById(String uuid);
    List<FileDTO> findAllFilesBySubjectId(Integer userId, Integer subjectId);
    void deleteFile(String uuid, Integer userId);
}
