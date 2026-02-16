package com.academo.controller;

import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.controller.dtos.subject.CreateSubjectDTO;
import com.academo.service.activity.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.academo.model.Activity;
import com.academo.model.Subject;
import com.academo.security.authuser.AuthUser;
import com.academo.service.subject.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/subjects")
@Tag(name = "Matérias")
public class SubjectController {

    @Autowired
    ISubjectService service;

    @Autowired
    IActivityService activityService;

    // A recuperação do Id do User por meio do PathVariable é temporária
    // Será implementado um Middleware para recuperação deste ID
    @Operation(summary = "Cadastra uma nova matéria", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Matéria cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar cadastrar matéria")
    })
    @PostMapping
    public ResponseEntity<Subject> create(Authentication authentication, @RequestBody CreateSubjectDTO subjectDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        Subject createdSubject = service.create(new Subject(subjectDTO.name(), subjectDTO.description()),userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Recupera as matérias de um grupo", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matérias recuperadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar matérias"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada para este grupo")
    })
    @GetMapping("/by-group")
    public ResponseEntity<List<SubjectDTO>> getByGroup(Authentication authentication, @RequestParam Integer groupId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        List<SubjectDTO> subjects = service.findByGroup(groupId)
                .stream()
                .map(s -> new SubjectDTO(
                        s.getId(),
                        s.getName(),
                        s.getDescription(),
                        s.getIsActive(),
                        s.getCreatedAt(),
                        s.getUpdatedAt())).toList();

        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Recupera a lista de todas as matérias de um usuário", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matérias recuperadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar matérias"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada")
    })
    @GetMapping("/all")
    public ResponseEntity<List<SubjectDTO>> getSubjects(Authentication authentication) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        List<SubjectDTO> subjects = service.findAll(userId)
                .stream()
                .map(g -> new SubjectDTO(
                        g.getId(),
                        g.getName(),
                        g.getDescription(),
                        g.getIsActive(),
                        g.getCreatedAt(),
                        g.getUpdatedAt())).toList();
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Recupera uma matéria", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matéria recuperada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar matéria"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada com este ID")
    })
    @GetMapping("one")
    public ResponseEntity<SubjectDTO> getSubject(Authentication authentication, @RequestParam Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        Subject subject = service.getSubjectByIdAndUserId(subjectId, userId);
        SubjectDTO subjectDTO = new SubjectDTO(subject.getId(), subject.getName(), subject.getDescription(), subject.getIsActive(),subject.getCreatedAt(), subject.getUpdatedAt());
        return ResponseEntity.ok(subjectDTO);
    }

    @Operation(summary = "Atualiza uma matéria", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matéria atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar matéria"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada com este ID")
    })
    @PutMapping
    public ResponseEntity<SubjectDTO> updateSubject(Authentication authentication, @RequestBody SubjectDTO subjectDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        Subject subject = new Subject(subjectDTO);
        subject.setActivities(activityService.getBySubjectId(subjectDTO.id()));
        service.updateSubject(userId,subject);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove uma matéria", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Matéria removida com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar remover matéria"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada com este ID")
    })
    @DeleteMapping
    public ResponseEntity<Activity> deleteActivity(Authentication authentication, @RequestParam Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        service.deleteSubject(userId, subjectId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
