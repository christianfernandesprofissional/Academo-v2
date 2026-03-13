package com.academo.service.file;

import com.academo.controller.dtos.file.FileDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.File;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.FileRepository;
import com.academo.service.subject.ISubjectService;
import com.academo.service.user.IUserService;
import com.academo.service.storage.google.DriveService;
import com.academo.util.exceptions.fileTransfer.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
public class FileServiceImpl implements IFileService {

    private final FileRepository fileRepository;
    private final IUserService userService;
    private final ISubjectService subjectService;
    private final DriveService driveService;

    public FileServiceImpl(FileRepository fileRepository, IUserService userService, ISubjectService subjectService, DriveService driveService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.subjectService = subjectService;
        this.driveService = driveService;
    }

    private static final long ONE_MB = 1024L * 1024L;

    private static final long FIFTEEN_MB = 15 * ONE_MB;

    private static final long THREE_HUNDRED_MB = 300 * ONE_MB;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/csv",
            "text/plain"
    );

    @Transactional
    @Override
    public FileDTO createFile(MultipartFile file, Integer userId, Integer subjectId) {

        Subject subject = SubjectDTO.toSubject(subjectId, subjectService.findById(subjectId, userId));
        User user = userService.findById(userId);
        isUserStorageFull(file, user);
        isMimeTypeValid(file);
        isFileSizeValid(file);


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
        return new FileDTO(
                uploadedFile.getUuid(),
                uploadedFile.getFileName(),
                uploadedFile.getPath(),
                uploadedFile.getFileType(),
                uploadedFile.getSize(),
                uploadedFile.getSubject().getId(),
                uploadedFile.getCreatedAt()
        );
    }

    @Override
    public File findFileById(String uuid){
        return fileRepository.findById(uuid).orElseThrow(FileNotFoundException::new);
    }

    @Override
    public List<FileDTO> findAllFilesBySubjectId(Integer userId, Integer subjectId) {
        //Não entendi o porquê deste subject. Imagino que seja para verificar caso ele não exista. Neste caso, esta primeira linha lançará exceção
        Subject subject = SubjectDTO.toSubject(subjectId, subjectService.findById(subjectId, userId));

        return fileRepository.findAllBySubjectId(subjectId).stream().map( file -> new FileDTO(
                file.getUuid(),
                file.getFileName(),
                file.getPath(),
                file.getFileType(),
                file.getSize(),
                file.getSubject().getId(),
                file.getCreatedAt())).toList();
    }

    @Transactional
    @Override
    public void deleteFile(String uuid, Integer userId) {
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


    // -------> MÉTODOS AUXILIARES <--------


    private Boolean isMimeTypeValid(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new MimeTypeException();
        }
        return true;
    }

    private Boolean isFileSizeValid(MultipartFile file) {
        //RETORNAR EXCEÇÃO
        if(file.getSize() > FIFTEEN_MB) throw new FileSizeException();
        return true;
    }

    private Boolean isUserStorageFull(MultipartFile file, User user) {
        if(user.getStorageUsage() + file.getSize() > THREE_HUNDRED_MB) {
            throw new UserStorageIsFullException();
        }
        return true;
    }
}
