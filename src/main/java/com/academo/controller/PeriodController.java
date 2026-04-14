package com.academo.controller;

import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.controller.dtos.period.UpdatePeriodDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.period.IPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/periods")
public class PeriodController {

    private final IPeriodService service;

    public PeriodController(IPeriodService service){
        this.service = service;
    }

    @Operation(summary = "Recupera todos os períodos de uma matéria", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Períodos recuperados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar períodos"),
            @ApiResponse(responseCode = "404", description = "Nenhum período encontrado")
    })
    @GetMapping("/all/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<List<PeriodDTO>> findAll(Authentication auth, @PathVariable Integer subjectId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAll(userId, subjectId));
    }

    @Operation(summary = "Recupera um período específico", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Período recuperado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar período"),
            @ApiResponse(responseCode = "404", description = "Período não encontrado")
    })
    @GetMapping("/{subjectId}/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> findById(Authentication auth, @PathVariable Integer subjectId, @PathVariable Integer periodId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findById(userId, periodId));
    }

    @Operation(summary = "Cria um novo período", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Período criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar criar período")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> create(Authentication auth, @RequestBody @Valid SavePeriodDTO periodDTO){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        PeriodDTO saved = service.create(userId, periodDTO);
        URI uri = URI.create("/periods/"+saved.id());
        return ResponseEntity.created(uri).body(saved);
    }

    @Operation(summary = "Atualiza um período existente", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Período atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar período"),
            @ApiResponse(responseCode = "404", description = "Período não encontrado")
    })
    @PutMapping("/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> update(Authentication auth,@PathVariable Integer periodId, @RequestBody @Valid UpdatePeriodDTO periodDTO){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        PeriodDTO updated = service.update(userId, periodId, periodDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Remove um período", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Período removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar remover período"),
            @ApiResponse(responseCode = "404", description = "Período não encontrado")
    })
    @DeleteMapping("/{subjectId}/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> delete(Authentication auth, @PathVariable Integer subjectId, @PathVariable Integer periodId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        service.delete(userId, subjectId, periodId);
        return ResponseEntity.noContent().build();
    }
}
