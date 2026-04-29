package com.academo.controller;

import com.academo.controller.dtos.file.DownloadS3FileDTO;
import com.academo.controller.dtos.file.FileDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.file.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final IFileService fileService;

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    public FileController(IFileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload-file/{subjectId}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<FileDTO> upload(@RequestParam("file") MultipartFile file, @PathVariable("subjectId") Integer subjectId, Authentication authentication){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        FileDTO uploadedFile = fileService.upload(file, userId, subjectId);
        URI uri = URI.create(uploadedFile.path());
        return ResponseEntity.created(uri).body(uploadedFile);
    }

    @GetMapping("/download/{fileUUID}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileUUID) {

        DownloadS3FileDTO fileDTO = fileService.downloadStream(fileUUID);

        InputStreamResource resource = new InputStreamResource(fileDTO.response());

        return ResponseEntity.ok()
                .contentLength(fileDTO.response().response().contentLength())
                .contentType(MediaType.parseMediaType(fileDTO.mimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDTO.fileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{uuid}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<String> delete(@PathVariable String uuid, Authentication authentication) {
            Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
            fileService.delete(uuid, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<FileDTO>> findAll(Authentication authentication, @PathVariable Integer subjectId, Pageable pageable){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(fileService.findAllBySubject(userId, subjectId, pageable));
    }
}
