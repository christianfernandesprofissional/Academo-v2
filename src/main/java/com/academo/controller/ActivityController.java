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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ActivityDTO>> findAll(Authentication authentication) {
       Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
       return ResponseEntity.ok(activityService.findAll(userId));
    }

    @GetMapping("/by-subject/{subjectId}")
    public ResponseEntity<List<ActivityDTO>> findAllBySubject(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.findAllBySubjectId(userId, subjectId));
    }

    @Operation(summary = "Recupera uma atividade", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atividade recuperada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhuma atividade encontrada com este ID")
    })
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityDTO> findById(Authentication authentication, @PathVariable Integer activityId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.findById(userId, activityId));
    }


    @Operation(summary = "Cadastra uma nova atividade", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atividade cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar cadastrar atividade")
    })
    @PostMapping
    public ResponseEntity<ActivityDTO> create(Authentication authentication, @RequestBody @Valid SaveActivityDTO activityDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityDTO created = activityService.create(userId, activityDTO);
        URI location = URI.create("/activities/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Recupera a lista de todas as atividades de um usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atividade atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhuma atividade encontrada com este ID")
    })
    @PutMapping("/{activityId}")
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
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Integer activityId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        activityService.delete(userId,activityId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
