package com.academo.controller;

import com.academo.controller.dtos.group.AssociateSubjectsDTO;
import com.academo.controller.dtos.group.GroupDTO;
import com.academo.controller.dtos.group.CreateGroupDTO;
import com.academo.controller.dtos.group.UpdateGroupDTO;
import com.academo.service.group.IGroupService;
import com.academo.model.Group;
import com.academo.security.authuser.AuthUser;
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
@RequestMapping(value = "/groups")
public class GroupController {

    private final IGroupService groupService;

    public GroupController(IGroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<GroupDTO>> findAll(Authentication authentication, @RequestParam(required = false) Boolean isActive, Pageable pageable){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.findAll(userId, isActive, pageable));
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> findById(Authentication authentication, @PathVariable Integer groupId){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.findById(userId, groupId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> create(Authentication authentication, @RequestBody @Valid CreateGroupDTO groupDTO){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        GroupDTO createdGroup = groupService.create(userId,groupDTO);
        URI uri  = URI.create("/groups/" + createdGroup.id());
        return ResponseEntity.created(uri).body(createdGroup);
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> update(Authentication authentication ,@PathVariable Integer groupId, @RequestBody @Valid UpdateGroupDTO updateGroupDTO){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.update(userId, groupId, updateGroupDTO));
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Group> delete(Authentication authentication, @PathVariable Integer groupId){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        groupService.delete(userId,groupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/add-subject/{groupId}/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> addSubject(Authentication authentication, @PathVariable Integer groupId, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.addSubject(userId, groupId, subjectId));
    }

    @DeleteMapping("/delete-subject/{groupId}/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> deleteSubject(Authentication authentication, @PathVariable Integer groupId, @PathVariable Integer subjectId){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.deleteSubject(userId, groupId, subjectId));
    }

    @PutMapping("associate-subjects/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<GroupDTO> associateSubjects(Authentication authentication, @PathVariable Integer groupId, @RequestBody @Valid AssociateSubjectsDTO associateSubjectsDTO){
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(groupService.associateSubjects(userId, groupId, associateSubjectsDTO));
    }

}
