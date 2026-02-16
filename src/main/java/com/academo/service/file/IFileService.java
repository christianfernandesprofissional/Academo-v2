package com.academo.service.file;

import com.academo.controller.dtos.file.FileDTO;
import com.academo.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {

    public FileDTO createFile(MultipartFile file, Integer userId, Integer subjectId);
    public File findFileById(String uuid);
    public List<FileDTO> findAllFilesBySubjectId(Integer subjectId);
    public void deleteFile(String uuid, Integer userId);
}
