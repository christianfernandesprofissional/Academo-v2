package com.academo.service.file;

import com.academo.controller.dtos.file.FileDTO;
import com.academo.model.File;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.List;

public interface IFileService {

    FileDTO upload(MultipartFile file, Integer userId, Integer subjectId);
    FileDTO findById(String uuid);
    List<FileDTO> findAllBySubject(Integer userId, Integer subjectId);
    ResponseInputStream<GetObjectResponse> downloadStream(String uuid);
    void delete(String uuid, Integer userId);
}
