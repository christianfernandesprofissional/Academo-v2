package com.academo.controller;


import com.academo.controller.dtos.flashcard.CreateFlashcardDTO;
import com.academo.controller.dtos.flashcard.FlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateFlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateLevelDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.flashcard.IFlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/flashcards")
@Tag(name = "Flashcard")
public class FlashcardController {

    @Autowired
    private final IFlashcardService service;

    public FlashcardController(IFlashcardService service ){
        this.service = service;
    }

    @Operation(summary = "Recupera a lista de todas os flashcards de um usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flashcards recuperados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar Flashcards"),
            @ApiResponse(responseCode = "404", description = "Nenhum Flashcard encontrado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<List<FlashcardDTO>> findAll(Authentication authentication) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAllByUserId(userId));
    }


    @Operation(summary = "Recupera a lista de todas os flashcards de um usuário pelo ID da matéria", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flashcards recuperados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar Flashcards"),
            @ApiResponse(responseCode = "404", description = "Nenhum Flashcard encontrado")
    })
    @GetMapping("/all/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<List<FlashcardDTO>> findAllBySubjectId(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAllBySubjectId(userId, subjectId));
    }

    @Operation(summary = "Recupera a lista de todas os flashcards de um usuário pelo ID da matéria e nível", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flashcards recuperados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar Flashcards"),
            @ApiResponse(responseCode = "404", description = "Nenhum Flashcard encontrado")
    })
    @GetMapping("/all/{subjectId}/{level}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<List<FlashcardDTO>> findAllByLevel(Authentication authentication, @PathVariable Integer subjectId, @PathVariable String level) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAllByLevel(userId, subjectId, level));
    }

    @Operation(summary = "Recupera uma flashcard", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flashcard recuperado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar Flashcard"),
            @ApiResponse(responseCode = "404", description = "Flashcard não encontrado")
    })
    @GetMapping("/{flashcardId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<FlashcardDTO> findById(Authentication authentication, @PathVariable Integer flashcardId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findById(userId, flashcardId));
    }


    @Operation(summary = "Cadastra um novo flashcard", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Flashcard cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar cadastrar flashcard")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<FlashcardDTO> create(Authentication authentication, @RequestBody @Valid CreateFlashcardDTO flashcardDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        FlashcardDTO created = service.create(userId, flashcardDTO);
        URI location = URI.create("/flashcards/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza um flashcard", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flashcard atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar Flashcard"),
            @ApiResponse(responseCode = "404", description = "Nenhum Flashcard encontrado com este ID")
    })
    @PutMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<FlashcardDTO> update(Authentication authentication, @RequestBody @Valid UpdateFlashcardDTO flashcardDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.update(userId, flashcardDTO));
    }

    @Operation(summary = "Atualiza somente o nível do flashcard", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flashcard atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar flashcard"),
            @ApiResponse(responseCode = "404", description = "Nenhum flashcard encontrado com este ID")
    })
    @PatchMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<FlashcardDTO> updateLeveL(Authentication authentication, @RequestBody @Valid UpdateLevelDTO levelDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.updateLevel(userId, levelDTO));
    }

    @Operation(summary = "Remove um flashcard", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flashcard removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar deletar flashcard"),
            @ApiResponse(responseCode = "404", description = "Nenhum flashcard encontrado com este ID")
    })
    @DeleteMapping("/{flashcardId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Integer flashcardId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        service.delete(userId,flashcardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
