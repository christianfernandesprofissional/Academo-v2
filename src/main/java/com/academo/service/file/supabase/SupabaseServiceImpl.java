package com.academo.service.file.supabase;

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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
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

        String fileUUIDName = UUID.randomUUID().toString();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileUUIDName)
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

        File f = new File(file.getOriginalFilename(), fileUUIDName, file.getContentType(), file.getSize());
        f.setUser(user);
        f.setSubject(subject);
        File createdFile = fileRepository.save(f);
        return FileDTO.fromFile(createdFile);

    }

    @Override
    public FileDTO findById(String uuid) {
        return FileDTO.fromFile(fileRepository.findById(uuid).orElseThrow(FileNotFoundException::new));

    }

    @Override
    public List<FileDTO> findAllBySubject(Integer userId, Integer subjectId) {
        return fileRepository.findAllBySubjectIdAndUserId(subjectId, userId).stream().map(FileDTO::fromFile).toList();
    }

    @Transactional
    @Override
    public void delete(String uuid, Integer userId) {
        File file = fileRepository.findByIdAndUserId(uuid, userId).orElseThrow(FileNotFoundException::new);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getPath())
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        fileRepository.delete(file);
    }

    public ResponseInputStream<GetObjectResponse> downloadStream(String fileUUIDName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileUUIDName)
                .build();

        return s3Client.getObject(getObjectRequest);
    }
}
