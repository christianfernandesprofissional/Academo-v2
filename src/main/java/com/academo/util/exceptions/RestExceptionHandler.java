package com.academo.util.exceptions;

import com.academo.controller.dtos.validation.ValidationErrors;
import com.academo.model.User;
import com.academo.security.service.TokenService;
import com.academo.service.user.IUserService;
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
import com.academo.service.mail.IMailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final IMailService mailService;
    private final TokenService tokenService;
    private final IUserService userService;

    public RestExceptionHandler(IMailService mailService, TokenService tokenService, IUserService userService) {
        this.mailService = mailService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        validationErrors.put(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrors(validationErrors));
    }

    //Activity
    @ExceptionHandler(ActivityNotFoundException.class)
    private ResponseEntity<ExceptionDTO> activityNotFoundHandler(ActivityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(ActivityExistsException.class)
    private ResponseEntity<ExceptionDTO> activityNotFoundHandler(ActivityExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(exception.getMessage()));
    }

    //Group
    @ExceptionHandler(GroupNotFoundException.class)
    private ResponseEntity<ExceptionDTO> groupNotFoundHandler(GroupNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(exception.getMessage()));
    }

    //Profile
    @ExceptionHandler(ProfileNotFoundException.class)
    private ResponseEntity<ExceptionDTO> profileNotFoundHandler(ProfileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(exception.getMessage()));
    }

    //Subject
    @ExceptionHandler(SubjectNotFoundException.class)
    private ResponseEntity<ExceptionDTO> subjectNotFoundHandler(SubjectNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(exception.getMessage()));
    }

    //ActivityType
    @ExceptionHandler(ActivityTypeNotFoundException.class)
    private ResponseEntity<ExceptionDTO> typeActivityNotFoundException(ActivityTypeNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(ActivityTypeExistsException.class)
    private ResponseEntity<ExceptionDTO> typeActivityNotFoundException(ActivityTypeExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(exception.getMessage()));
    }

    //User
    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ExceptionDTO> userNotFoundHandler(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(ExistingUserException.class)
    private ResponseEntity<ExceptionDTO> existingUserHandler(ExistingUserException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(WrongDataException.class)
    private ResponseEntity<ExceptionDTO> wrongDataHandler(WrongDataException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(exception.getMessage()));
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
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDTO(exception.getMessage()));
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