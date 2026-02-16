package com.academo.controller;

import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.activityType.IActivityTypeService;
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
@RequestMapping("/activity-types")
@Tag(name = "Tipos de Atividade")
public class ActivityTypeController {

    private final IActivityTypeService activityTypeService;

    public ActivityTypeController(IActivityTypeService activityService){
        this.activityTypeService = activityService;
    }

    @Operation(summary = "Recupera a lista de todos os tipos de atividade de um usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos de Atividade recuperados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar tipos de atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de atividade encontrada")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ActivityTypeDTO>> findAll(Authentication authentication) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        List<ActivityTypeDTO> types  = activityTypeService.findAll(userId);
        return ResponseEntity.ok(types);
    }

    @Operation(summary = "Recupera um tipo de atividade de um usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de Atividade recuperado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar tipo de atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de atividade encontrado com este ID")
    })
    @GetMapping
    public ResponseEntity<ActivityTypeDTO> findById(Authentication authentication, @RequestParam Integer id) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityTypeDTO activityTypeDTO = activityTypeService.findDTO(id, userId);
        return ResponseEntity.ok(activityTypeDTO);
    }

    @Operation(summary = "Cadastra um tipo de atividade", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de atividade cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar cadastrar tipo de atividade"),
    })
    @PostMapping
    public ResponseEntity<ActivityTypeDTO> create(Authentication authentication, @RequestBody @Valid SaveActivityTypeDTO activityTypeDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityTypeDTO createdActivityType = activityTypeService.create(userId, activityTypeDTO);
        URI uri = URI.create("/activity-types?id=" + createdActivityType.id());
        return ResponseEntity.created(uri).body(createdActivityType);
    }

    @Operation(summary = "Atualiza um tipo de atividade", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de atividade atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar tipo de atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de atividade encontrado com este ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActivityTypeDTO> update(Authentication authentication,@PathVariable Integer id, @RequestBody @Valid SaveActivityDTO activityTypeDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityTypeDTO updated = activityTypeService.update(userId, id, activityTypeDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Remove um tipo de atividade", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de atividade deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar deletar tipo de atividade"),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de atividade encontrado com este ID")
    })
    @DeleteMapping
    public ResponseEntity<Void> delete(Authentication authentication, @RequestParam Integer activityTypeId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        activityTypeService.delete(userId, activityTypeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
