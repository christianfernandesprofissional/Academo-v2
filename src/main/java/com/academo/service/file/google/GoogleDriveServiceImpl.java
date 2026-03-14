package com.academo.service.file.google;

import com.academo.controller.dtos.file.FileDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.File;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.FileRepository;
import com.academo.service.file.IFileService;
import com.academo.service.subject.ISubjectService;
import com.academo.service.user.IUserService;
import com.academo.service.storage.google.DriveService;
import com.academo.util.config.storage.FileValidation;
import com.academo.util.exceptions.fileTransfer.*;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.List;


public class GoogleDriveServiceImpl implements IFileService {

    private final FileRepository fileRepository;
    private final IUserService userService;
    private final ISubjectService subjectService;
    private final DriveService driveService;

    public GoogleDriveServiceImpl(FileRepository fileRepository, IUserService userService, ISubjectService subjectService, DriveService driveService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.subjectService = subjectService;
        this.driveService = driveService;
    }


    @Transactional
    @Override
    public FileDTO upload(MultipartFile file, Integer userId, Integer subjectId) {

        Subject subject = SubjectDTO.toSubject(subjectId, subjectService.findById(subjectId, userId));
        User user = userService.findById(userId);
        FileValidation.isUserStorageFull(file, user);
        FileValidation.isMimeTypeValid(file);
        FileValidation.isFileSizeValid(file);


        String driveFileId = null;
        try {
            driveFileId = driveService.uploadFile(file);
        } catch (Exception e) {
            throw new FileStorageException("Erro ao fazer o upload do arquivo!");
        }

        Long newStorage = user.getStorageUsage() + file.getSize();
        user.setStorageUsage(newStorage);
        userService.update(user);

        String completePath = "files/download/" + driveFileId;
        File f = new File(file.getOriginalFilename(), completePath, file.getContentType(), file.getSize());
        f.setUser(user);
        f.setSubject(subject);

        File uploadedFile = fileRepository.save(f);
        return FileDTO.fromFile(uploadedFile);
    }

    @Override
    public FileDTO findById(String uuid){
        return FileDTO.fromFile(fileRepository.findById(uuid).orElseThrow(FileNotFoundException::new));
    }

    @Override
    public List<FileDTO> findAllBySubject(Integer userId, Integer subjectId) {
        return fileRepository.findAllBySubjectIdAndUserId(subjectId, userId).stream().map(FileDTO::fromFile).toList();
    }

    @Override
    public ResponseInputStream<GetObjectResponse> downloadStream(String uuid) {
        return null;
    }

    @Transactional
    @Override
    public void delete(String uuid, Integer userId) {
        User user = userService.findById(userId);
        File file = fileRepository.findById(uuid).orElseThrow(FileNotFoundException::new);

        long newUserStorage = user.getStorageUsage() - file.getSize();
        user.setStorageUsage(newUserStorage);

        String drivePath = file.getPath().substring(37);

        fileRepository.deleteById(uuid);
        userService.update(user);
        try {
            driveService.deleteFile(drivePath);
        } catch (Exception e) {
           throw new FileStorageException("Erro ao deletar arquivo!");
        }
    }
}
