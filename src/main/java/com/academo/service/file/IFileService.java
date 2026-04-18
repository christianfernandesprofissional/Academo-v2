package com.academo.service.file;

import com.academo.controller.dtos.file.DownloadGoogleFileDTO;
import com.academo.controller.dtos.file.DownloadS3FileDTO;
import com.academo.controller.dtos.file.FileDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {

    FileDTO upload(MultipartFile file, Integer userId, Integer subjectId);
    FileDTO findById(String uuid);
    Page<FileDTO> findAllBySubject(Integer userId, Integer subjectId, Pageable pageable);
    void delete(String uuid, Integer userId);
    DownloadS3FileDTO downloadStream(String fileUUID);
    DownloadGoogleFileDTO downloadGoogleFile(String fileUUID) throws Exception;
}
