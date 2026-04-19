package com.academo.service.file.supabase;

import com.academo.controller.dtos.file.DownloadGoogleFileDTO;
import com.academo.controller.dtos.file.DownloadS3FileDTO;
import com.academo.controller.dtos.file.FileDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.File;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.FileRepository;
import com.academo.service.file.IFileService;
import com.academo.service.subject.ISubjectService;
import com.academo.service.user.IUserService;
import com.academo.util.config.storage.FileValidation;
import com.academo.util.exceptions.fileTransfer.FileNotFoundException;
import com.academo.util.exceptions.fileTransfer.FileStorageException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class SupabaseServiceImpl implements IFileService {

    private final S3Client s3Client;
    private final String bucketName;
    private final IUserService userService;
    private final ISubjectService subjectService;
    private final FileRepository fileRepository;


    public SupabaseServiceImpl(S3Client s3Client, @Value("${storage.bucket}") String bucketName, IUserService userService, ISubjectService subjectService, FileRepository fileRepository) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.userService = userService;
        this.subjectService = subjectService;
        this.fileRepository = fileRepository;
    }

    @Transactional
    @Override
    public FileDTO upload(MultipartFile file, Integer userId, Integer subjectId) {

        Subject subject = SubjectDTO.toSubject(subjectId, subjectService.findById(subjectId, userId));
        User user = userService.findById(userId);
        FileValidation.isUserStorageFull(file, user);
        FileValidation.isMimeTypeValid(file);
        FileValidation.isFileSizeValid(file);

        String pathUUID = UUID.randomUUID().toString();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(pathUUID)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch(S3Exception e) {
            throw new FileStorageException("Erro ao tentar fazer upload do arquivo: " + e.getMessage());
        } catch (IOException e) {
            throw new FileStorageException("Erro ao fazer leitura do arquivo: " +  e.getMessage());
        }

        user.increaseStorageUsage(file.getSize());
        userService.update(user);

        File f = new File(file.getOriginalFilename(), pathUUID, file.getContentType(), file.getSize());
        f.setUser(user);
        f.setSubject(subject);
        File createdFile = fileRepository.save(f);
        return FileDTO.fromFile(createdFile);

    }

    @Override
    public FileDTO findById(String uuid) {
        return FileDTO.fromFile(fileRepository.findById(UUID.fromString(uuid)).orElseThrow(FileNotFoundException::new));

    }

    @Override
    public Page<FileDTO> findAllBySubject(Integer userId, Integer subjectId, Pageable pageable) {
        return fileRepository.findAllBySubjectIdAndUserId(subjectId, userId, pageable).map(FileDTO::fromFile);
    }

    @Transactional
    @Override
    public void delete(String uuid, Integer userId) {
        File file = fileRepository.findByUuidAndUserId(UUID.fromString(uuid), userId).orElseThrow(FileNotFoundException::new);
        User user = userService.findById(userId);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getPath())
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        user.decreaseStorageUsage(file.getSize());
        userService.update(user);
        fileRepository.delete(file);
    }

    @Override
    public DownloadS3FileDTO downloadStream(String fileUUID) {
        File file = fileRepository.findById(UUID.fromString(fileUUID)).orElseThrow(FileNotFoundException::new);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getPath())
                .build();

        return new DownloadS3FileDTO(
                file.getFileName(),
                file.getFileType(),
                file.getSize(),
                s3Client.getObject(getObjectRequest));
    }

    @Override
    public DownloadGoogleFileDTO downloadGoogleFile(String fileUUID) throws Exception {
        return null;
    }
}
