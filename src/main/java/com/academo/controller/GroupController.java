package com.academo.controller;

import com.academo.controller.dtos.group.AssociateSubjectsDTO;
import com.academo.controller.dtos.group.GroupDTO;
import com.academo.controller.dtos.group.CreateGroupDTO;
import com.academo.controller.dtos.group.UpdateGroupDTO;
import com.academo.service.group.IGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.academo.model.Group;
import com.academo.security.authuser.AuthUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/groups")
@Tag(name = "Grupos")
public class GroupController {

    private final IGroupService groupService;

    public GroupController(IGroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "Recupera a lista de todos os grupos de um usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupos recuperados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar grupos"),
            @ApiResponse(responseCode = "404", description = "Nenhum grupo encontrado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<List<GroupDTO>> findAll(Authentication authentication){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.findAll(userId));
    }

    @Operation(summary = "Recupera um grupo", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo recuperado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar grupo"),
            @ApiResponse(responseCode = "404", description = "Nenhum grupo encontrado com este ID")
    })
    @GetMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> findById(Authentication authentication, @PathVariable Integer groupId){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.findById(userId, groupId));
    }

    @Operation(summary = "Cadastra um novo grupo", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Grupo cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar cadastrar grupo")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> create(Authentication authentication, @RequestBody @Valid CreateGroupDTO groupDTO){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        GroupDTO createdGroup = groupService.create(userId,groupDTO);
        URI uri  = URI.create("/groups/" + createdGroup.id());
        return ResponseEntity.created(uri).body(createdGroup);
    }

    @Operation(summary = "Atualiza um grupo", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar grupo"),
            @ApiResponse(responseCode = "404", description = "Nenhum grupo encontrado com este ID")
    })
    @PutMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> update(Authentication authentication ,@PathVariable Integer groupId, @RequestBody @Valid UpdateGroupDTO updateGroupDTO){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.update(userId, groupId, updateGroupDTO));
    }

    @Operation(summary = "Remove um grupo", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Grupo removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar deletar grupo"),
            @ApiResponse(responseCode = "404", description = "Nenhum grupo encontrado com este ID")
    })
    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Group> delete(Authentication authentication, @PathVariable Integer groupId){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        groupService.delete(userId,groupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Adiciona uma matéria a um grupo", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matéria adicionada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar adicionar matéria"),
            @ApiResponse(responseCode = "404", description = "Grupo ou matéria não encontrado")
    })
    @PostMapping("/add-subject/{groupId}/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> addSubject(Authentication authentication, @PathVariable Integer groupId, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.addSubject(userId, groupId, subjectId));
    }

    @Operation(summary = "Remove uma matéria de um grupo", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matéria removida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar remover matéria"),
            @ApiResponse(responseCode = "404", description = "Grupo ou matéria não encontrado")
    })
    @DeleteMapping("/delete-subject/{groupId}/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> deleteSubject(Authentication authentication, @PathVariable Integer groupId, @PathVariable Integer subjectId){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.deleteSubject(userId, groupId, subjectId));
    }


    @Operation(summary = "Associa matérias a um grupo", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar associar matérias"),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado")
    })
    @PutMapping("associate-subjects/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> associateSubjects(Authentication authentication, @PathVariable Integer groupId, @RequestBody @Valid AssociateSubjectsDTO associateSubjectsDTO){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.associateSubjects(userId, groupId, associateSubjectsDTO));
    }

}
