package com.academo.controller;

import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.controller.dtos.subject.CreateSubjectDTO;
import com.academo.controller.dtos.subject.UpdateSubjectDTO;
import com.academo.service.activity.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.academo.model.Activity;
import com.academo.security.authuser.AuthUser;
import com.academo.service.subject.ISubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<SubjectDTO> create(Authentication authentication, @Valid @RequestBody CreateSubjectDTO createSubjectDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        SubjectDTO createdSubject = service.create(userId, createSubjectDTO);
        URI uri = URI.create("/subjects/" + createdSubject.id());
        return ResponseEntity.created(uri).body(createdSubject);
    }

    @Operation(summary = "Recupera as matérias de um grupo", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matérias recuperadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar matérias"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada para este grupo")
    })
    @GetMapping("/in-group/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<List<SubjectDTO>> findByGroupId(Authentication authentication, @PathVariable Integer groupId) {
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
    @GetMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
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
    @GetMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<SubjectDTO> findById(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        SubjectDTO subjectDTO = service.findById(subjectId, userId);
        return ResponseEntity.ok(subjectDTO);
    }

    @Operation(summary = "Atualiza uma matéria", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matéria atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar matéria"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada com este ID")
    })
    @PutMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<SubjectDTO> update(Authentication authentication, @PathVariable Integer subjectId, @Valid @RequestBody UpdateSubjectDTO updateSubjectDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.update(userId, subjectId, updateSubjectDTO));
    }

    @Operation(summary = "Remove uma matéria", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Matéria removida com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar remover matéria"),
        @ApiResponse(responseCode = "404", description = "Nenhuma matéria encontrada com este ID")
    })
    @DeleteMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Activity> delete(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        service.delete(userId, subjectId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
