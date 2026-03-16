package com.academo.service.file.google;

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
import com.academo.util.exceptions.fileTransfer.*;
import com.google.api.client.http.InputStreamContent;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.google.api.services.drive.Drive;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

//@Component
public class GoogleDriveServiceImpl implements IFileService {

    private final FileRepository fileRepository;
    private final IUserService userService;
    private final ISubjectService subjectService;
    private final Drive drive;

    public GoogleDriveServiceImpl(FileRepository fileRepository, IUserService userService, ISubjectService subjectService, Drive drive) {
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.subjectService = subjectService;
        this.drive = drive;
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
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setName(file.getOriginalFilename());

            com.google.api.services.drive.model.File uploadedFile = drive.files().create(
                    fileMetadata,
                    new InputStreamContent(
                            file.getContentType(),
                            file.getInputStream()
                    )
            ).setFields("id").execute();

            driveFileId = uploadedFile.getId();
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
        return FileDTO.fromFile(fileRepository.findById(UUID.fromString(uuid)).orElseThrow(FileNotFoundException::new));
    }

    @Override
    public List<FileDTO> findAllBySubject(Integer userId, Integer subjectId) {
        return fileRepository.findAllBySubjectIdAndUserId(subjectId, userId).stream().map(FileDTO::fromFile).toList();
    }

    @Transactional
    @Override
    public void delete(String uuid, Integer userId) {
        User user = userService.findById(userId);
        File file = fileRepository.findById(UUID.fromString(uuid)).orElseThrow(FileNotFoundException::new);

        long newUserStorage = user.getStorageUsage() - file.getSize();
        user.setStorageUsage(newUserStorage);

        String drivePath = file.getPath().substring(37);

        fileRepository.deleteById(UUID.fromString(uuid));
        userService.update(user);
        try {
            drive.files().delete(drivePath).execute();
        } catch (Exception e) {
           throw new FileStorageException("Erro ao deletar arquivo!");
        }
    }

    @Override
    public DownloadS3FileDTO downloadStream(String fileUUIDName) {
        return null;
    }


    @Override
    public DownloadGoogleFileDTO downloadGoogleFile(String fileId) throws Exception {
        // Recupera o metadado do arquivo
        com.google.api.services.drive.model.File fileMetadata = drive.files()
                .get(fileId)
                .setFields("name, mimeType")
                .execute();

        // Cria o fluxo de saída para armazenar os bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Faz o download do conteúdo
        drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);

        // Retorna um objeto com as informações do arquivo
        return new DownloadGoogleFileDTO(
                fileMetadata.getName(),
                fileMetadata.getMimeType(),
                outputStream.toByteArray()
        );
    }
}
