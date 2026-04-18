package com.academo.controller;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.model.Activity;
import com.academo.security.authuser.AuthUser;
import com.academo.service.activity.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/activities")
@Tag(name = "Atividades")
public class ActivityController {

    private final IActivityService activityService;

    public ActivityController(IActivityService activityService) {
        this.activityService = activityService;
    }


    @Operation(summary = "Recupera a lista de todas as atividades de um usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atividades recuperadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar atividades"),
            @ApiResponse(responseCode = "404", description = "Nenhuma atividade encontrada")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<ActivityDTO>> findAll(Authentication authentication, Pageable pageable) {
       Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
       return ResponseEntity.ok(activityService.findAll(userId, pageable));
    }


    @Operation(summary = "Recupera uma atividade", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atividade recuperada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhuma atividade encontrada com este ID")
    })
    @GetMapping("/{activityId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityDTO> findById(Authentication authentication, @PathVariable Integer activityId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.findById(userId, activityId));
    }


    @GetMapping("/by-subject/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<ActivityDTO>> findAllBySubject(Authentication authentication, @PathVariable Integer subjectId, Pageable pageable) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.findAllBySubjectId(userId, subjectId, pageable));
    }

    @Operation(summary = "Cadastra uma nova atividade", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atividade cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar cadastrar atividade")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityDTO> create(Authentication authentication, @RequestBody @Valid SaveActivityDTO activityDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityDTO created = activityService.create(userId, activityDTO);
        URI location = URI.create("/activities/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza uma atividade do usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atividade atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhuma atividade encontrada com este ID")
    })
    @PutMapping("/{activityId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityDTO> update(Authentication authentication,@PathVariable Integer activityId, @RequestBody @Valid SaveActivityDTO activityDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.update(userId, activityId, activityDTO));
    }

    @Operation(summary = "Remove uma atividade", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Atividade removida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar deletar atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhuma atividade encontrada com este ID")
    })
    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Integer activityId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        activityService.delete(userId,activityId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
