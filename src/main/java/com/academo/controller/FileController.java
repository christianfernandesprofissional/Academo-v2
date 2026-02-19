package com.academo.controller;

import com.academo.controller.dtos.file.FileDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.file.IFileService;
import com.academo.service.storage.google.DriveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/files")
@Tag(name = "Arquivos")
public class FileController {

    private final IFileService fileService;
    private final DriveService driveService;

    public FileController(IFileService fileService, DriveService driveService) {
        this.fileService = fileService;
        this.driveService = driveService;
    }

    @Operation(summary = "Realiza o Upload de um arquivo", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Upload realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar realizar upload")
    })
    @PostMapping("/upload-file/{file}/{subjectId}")
    public ResponseEntity<FileDTO> upload(@PathVariable("file") MultipartFile file, @PathVariable("subjectId") Integer subjectId, Authentication authentication){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        FileDTO uploadedFile = fileService.createFile(file, userId, subjectId);
        URI uri = URI.create(uploadedFile.path());
        return ResponseEntity.created(uri).body(uploadedFile);
    }

    @Operation(summary = "Realiza o download de um arquivo", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Download realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar realizar download do arquivo"),
            @ApiResponse(responseCode = "404", description = "Nenhum arquivo encontrado com este ID")
    })
    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileId) {
        DriveService.DownloadedFile downloaded = null;
        try {
            downloaded = driveService.getFile(fileId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ByteArrayResource resource = new ByteArrayResource(downloaded.content());
        String mimeType = downloaded.mimeType() != null ? downloaded.mimeType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloaded.name() + "\"")
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }

    @Operation(summary = "Remove um arquivo", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar remover o arquivo"),
            @ApiResponse(responseCode = "404", description = "Nenhuma arquivo encontrado com este ID")
    })
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteFile(@PathVariable String uuid, Authentication authentication) {
            Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
            fileService.deleteFile(uuid, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "Recupera a lista de arquivos de uma atividade", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivos recuperados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar arquivos"),
            @ApiResponse(responseCode = "404", description = "Nenhum arquivo encontrado")
    })
    @GetMapping("/{subjectId}")
    public ResponseEntity<List<FileDTO>> findAll(Authentication authentication, @PathVariable Integer subjectId){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(fileService.findAllFilesBySubjectId(userId, subjectId));
    }





}
