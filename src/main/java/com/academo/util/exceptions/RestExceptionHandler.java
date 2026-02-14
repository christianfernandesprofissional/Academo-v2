package com.academo.util.exceptions;

import com.academo.model.User;
import com.academo.security.service.TokenService;
import com.academo.service.user.UserServiceImpl;
import com.academo.util.exceptions.FileTransfer.*;
import com.academo.util.exceptions.activity.ActivityExistsException;
import com.academo.util.exceptions.activity.ActivityNotFoundException;
import com.academo.util.exceptions.activityType.ActivityTypeExistsException;
import com.academo.util.exceptions.activityType.ActivityTypeNotFoundException;
import com.academo.controller.dtos.exception.ExceptionDTO;
import com.academo.util.exceptions.group.GroupNotFoundException;
import com.academo.util.exceptions.profile.ProfileNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import com.academo.util.exceptions.user.*;
import com.academo.util.mailservice.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private IMailService mailService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserServiceImpl userService;

    //Activity
    @ExceptionHandler(ActivityNotFoundException.class)
    private ResponseEntity<ExceptionDTO> activityNotFoundHandler(ActivityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("Atividade não encontrada!"));
    }

    @ExceptionHandler(ActivityExistsException.class)
    private ResponseEntity<ExceptionDTO> activityNotFoundHandler(ActivityExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Atividade já cadastrada!"));
    }

    //Group
    @ExceptionHandler(GroupNotFoundException.class)
    private ResponseEntity<ExceptionDTO> groupNotFoundHandler(GroupNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("Grupo de matérias não encontrado!"));
    }

    //Profile
    @ExceptionHandler(ProfileNotFoundException.class)
    private ResponseEntity<ExceptionDTO> profileNotFoundHandler(ProfileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("Perfil não encontrado!"));
    }

    //Subject
    @ExceptionHandler(SubjectNotFoundException.class)
    private ResponseEntity<ExceptionDTO> subjectNotFoundHandler(SubjectNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("Matéria não encontrada!"));
    }

    //ActivityType
    @ExceptionHandler(ActivityTypeNotFoundException.class)
    private ResponseEntity<ExceptionDTO> typeActivityNotFoundException(ActivityTypeNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("Tipo de Atividade não encontrado!"));
    }

    @ExceptionHandler(ActivityTypeExistsException.class)
    private ResponseEntity<ExceptionDTO> typeActivityNotFoundException(ActivityTypeExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Tipo de Atividade já existe!"));
    }

    //User
    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ExceptionDTO> userNotFoundHandler(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("Usuário não encontrado!"));
    }

    @ExceptionHandler(ExistingUserException.class)
    private ResponseEntity<ExceptionDTO> existingUserHandler(ExistingUserException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Este usuário já está cadastrado no sistema!"));
    }

    @ExceptionHandler(WrongDataException.class)
    private ResponseEntity<ExceptionDTO> wrongDataHandler(WrongDataException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Usuário/Email ou Senha Inválidos!"));
    }


    //Inserção ou deleção de objetos que não pertecem ao usuário
    @ExceptionHandler(NotAllowedInsertionException.class)
    private ResponseEntity<ExceptionDTO> userNotFoundHandler(NotAllowedInsertionException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(UserIsNotActiveException.class)
    private ResponseEntity<ExceptionDTO> userIsNotActiveHandler(UserIsNotActiveException exception) {
        User user = exception.getUser();
        if (!user.getActivationAccountTokenExpiration().isAfter(LocalDateTime.now())) {
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30).plusSeconds(20).atOffset(ZoneOffset.of("-03:00")).toLocalDateTime();
            user.setActivationAccountTokenExpiration(expiresAt);
            userService.update(user);
            var token = tokenService.generateActivationToken(user.getId());
            mailService.sendActivationMail(user.getEmail(), token);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDTO("O usuário ainda não foi ativado. Confira seu email para ativar"));
    }

    //Files
    @ExceptionHandler(FileSizeException.class)
    private ResponseEntity<ExceptionDTO> fileSizeHandler(FileSizeException exception) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(FileNotFoundException.class)
    private ResponseEntity<ExceptionDTO> fileSizeHandler(FileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(MimeTypeException.class)
    private ResponseEntity<ExceptionDTO> mimeTypeHandler(MimeTypeException exception) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(UserStorageIsFullException.class)
    private ResponseEntity<ExceptionDTO> userStorageIsFullHandler(UserStorageIsFullException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(FileStorageException.class)
    private ResponseEntity<ExceptionDTO> userStorageIsFullHandler(FileStorageException exception) {
        // Se tiver erro de autenticação do google, apague o diretório tokens, e reinicie a aplicação
        //isso vai fazer a aplicação gerar o link de login do google novamente
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(AlreadyActivatedUserException.class)
    private ResponseEntity<ExceptionDTO> alreadyActivatedUserHandler(AlreadyActivatedUserException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDTO(exception.getMessage()));
    }

}