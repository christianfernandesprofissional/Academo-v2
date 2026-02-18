package com.academo.controller;

import com.academo.controller.dtos.group.AssociateSubjectsDTO;
import com.academo.controller.dtos.group.GroupDTO;
import com.academo.controller.dtos.group.CreateGroupDTO;
import com.academo.controller.dtos.group.UpdateGroupDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.service.group.IGroupService;
import com.academo.service.subject.ISubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.academo.model.Group;
import com.academo.security.authuser.AuthUser;
import com.academo.service.group.GroupServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/groups")
@Tag(name = "Grupos")
public class GroupController {

    private final IGroupService groupService;
    private final ISubjectService subjectService;

    public GroupController(IGroupService groupService, ISubjectService subjectService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
    }

    @Operation(summary = "Recupera a lista de todos os grupos de um usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupos recuperados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar recuperar grupos"),
            @ApiResponse(responseCode = "404", description = "Nenhum grupo encontrado")
    })
    @GetMapping("/all")
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
    public ResponseEntity<GroupDTO> findById(Authentication authentication, @PathVariable Integer groupId){
        //usando @RequestParam a requisição é feita pela url ficando localhost:8080/groups?groupId=1
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.findById(userId, groupId));
    }

    @Operation(summary = "Associa matérias a um grupo", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar associar matérias"),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado")
    })
    @PutMapping("associate-subjects")
    public ResponseEntity<GroupDTO> associateSubjects(Authentication authentication, @RequestBody AssociateSubjectsDTO associateSubjectsDTO){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        Group g = groupService.associateSubjects(userId, associateSubjectsDTO.groupId(), associateSubjectsDTO.subjectsIds());
        Group group = groupService.updateGroup(userId, g);
        List<SubjectDTO> subjects = group.getSubjects().stream()
                .map(s -> new SubjectDTO(
                        s.getId(),
                        s.getName(),
                        s.getDescription(),
                        s.getIsActive(),
                        s.getCreatedAt(),
                        s.getUpdatedAt()
                )).toList();
        GroupDTO groupDTO = new GroupDTO(group.getId(), group.getName(), group.getDescription(), group.getIsActive(), subjects);
        return ResponseEntity.ok(groupDTO);
    }

    @Operation(summary = "Cadastra um novo grupo", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Grupo cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar cadastrar grupo")
    })
    @PostMapping
    public ResponseEntity<GroupDTO> create(Authentication authentication, @RequestBody CreateGroupDTO groupDTO){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        groupService.create(userId,groupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualiza um grupo", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar atualizar grupo"),
            @ApiResponse(responseCode = "404", description = "Nenhum grupo encontrado com este ID")
    })
    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDTO> update(Authentication authentication ,@PathVariable Integer groupId, @RequestBody UpdateGroupDTO updateGroupDTO){
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
    public ResponseEntity<Group> deleteGroup(Authentication authentication, @PathVariable Integer groupId){
        //usando @RequestParam a requisição é feita pela url ficando localhost:8080/groups?groupId=1
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        groupService.deleteGroup(userId,groupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Adiciona uma matéria a um grupo", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matéria adicionada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar adicionar matéria"),
            @ApiResponse(responseCode = "404", description = "Grupo ou matéria não encontrado")
    })
    @PostMapping("/addSubject")
    public ResponseEntity<GroupDTO> addSubjectToGroup(Authentication authentication, @RequestParam Integer groupId, @RequestParam Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        Group group = groupService.addSubjectToGroup(userId, groupId, subjectId);
        List<SubjectDTO> subjects = group.getSubjects().stream()
                .map(s -> new SubjectDTO(
                        s.getId(),
                        s.getName(),
                        s.getDescription(),
                        s.getIsActive(),
                        s.getCreatedAt(),
                        s.getUpdatedAt()
                )).toList();
        GroupDTO groupDTO = new GroupDTO(group.getId(), group.getName(), group.getDescription(), group.getIsActive(), subjects);
        return ResponseEntity.ok(groupDTO);
    }

    @Operation(summary = "Remove uma matéria de um grupo", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matéria removida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar remover matéria"),
            @ApiResponse(responseCode = "404", description = "Grupo ou matéria não encontrado")
    })
    @DeleteMapping("/remove-subject")
    public ResponseEntity<GroupDTO> removeSubjectFromGroup(Authentication authentication, @RequestParam Integer groupId, @RequestParam Integer subjectId){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        Group group = groupService.deleteSubjectFromGroup(userId, groupId, subjectId);
        List<SubjectDTO> subjects = group.getSubjects().stream()
                .map(s -> new SubjectDTO(
                        s.getId(),
                        s.getName(),
                        s.getDescription(),
                        s.getIsActive(),
                        s.getCreatedAt(),
                        s.getUpdatedAt()
                )).toList();
        GroupDTO groupDTO = new GroupDTO(group.getId(), group.getName(), group.getDescription(), group.getIsActive(), subjects);
        return ResponseEntity.ok(groupDTO);
    }
}
