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

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/subjects")
@Tag(name = "Matérias")
public class SubjectController {


    private final ISubjectService service;


    public SubjectController(ISubjectService subjectService, IActivityService activityService){
        service = subjectService;
    }

    // A recuperação do Id do User por meio do PathVariable é temporária
    // Será implementado um Middleware para recuperação deste ID
    @Operation(summary = "Cadastra uma nova matéria", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Matéria cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar cadastrar matéria")
    })
    @PostMapping
    public ResponseEntity<SubjectDTO> create(Authentication authentication, @RequestBody CreateSubjectDTO subjectDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        SubjectDTO createdSubject = service.create(subjectDTO.name(),subjectDTO.description(),userId);
        URI uri = URI.create("/subjects?subjectId=" + createdSubject.id());
        return ResponseEntity.created(uri).body(createdSubject);
    }

    @Operation(summary = "Recupera as matérias de um grupo", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matérias recuperadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar matérias"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada para este grupo")
    })
    @GetMapping("/in-group")
    public ResponseEntity<List<SubjectDTO>> findSubjectByGroupId(Authentication authentication, @PathVariable Integer groupId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        List<SubjectDTO> subjects = service.findByGroup(groupId);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Recupera a lista de todas as matérias de um usuário", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matérias recuperadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar matérias"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada")
    })
    @GetMapping("/all")
    public ResponseEntity<List<SubjectDTO>> findAll(Authentication authentication) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        List<SubjectDTO> subjects = service.findAll(userId);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Recupera uma matéria", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matéria recuperada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar matéria"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada com este ID")
    })
    @GetMapping
    public ResponseEntity<SubjectDTO> findById(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        SubjectDTO subjectDTO = service.findBySubjectId(subjectId, userId);
        return ResponseEntity.ok(subjectDTO);
    }

    @Operation(summary = "Atualiza uma matéria", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matéria atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar matéria"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada com este ID")
    })
    @PutMapping
    public ResponseEntity<SubjectDTO> update(Authentication authentication, @RequestBody SubjectDTO subjectDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        SubjectDTO dto = service.updateSubject(userId, subjectDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove uma matéria", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Matéria removida com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar remover matéria"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada com este ID")
    })
    @DeleteMapping
    public ResponseEntity<Activity> delete(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        service.deleteSubject(userId, subjectId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
