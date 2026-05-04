package com.academo.controller;

import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.controller.dtos.subject.CreateSubjectDTO;
import com.academo.controller.dtos.subject.UpdateSubjectDTO;
import com.academo.controller.dtos.subject.SubjectWithFlashcardDTO;
import com.academo.controller.dtos.subject.SubjectWithPeriodDTO;
import com.academo.service.activity.IActivityService;
import com.academo.model.Activity;
import com.academo.security.authuser.AuthUser;
import com.academo.service.subject.ISubjectService;
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
@RequestMapping("/subjects")
public class SubjectController {


    private final ISubjectService service;

    public SubjectController(ISubjectService subjectService, IActivityService activityService){
        service = subjectService;
    }

    // A recuperação do Id do User por meio do PathVariable é temporária
    // Será implementado um Middleware para recuperação deste ID
    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<SubjectDTO> create(Authentication authentication, @Valid @RequestBody CreateSubjectDTO createSubjectDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        SubjectDTO createdSubject = service.create(userId, createSubjectDTO);
        URI uri = URI.create("/subjects/" + createdSubject.id());
        return ResponseEntity.created(uri).body(createdSubject);
    }

    @GetMapping("/in-group/{groupId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<SubjectDTO>> findByGroupId(Authentication authentication, @PathVariable Integer groupId, Pageable pageable) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findByGroup(groupId, pageable));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<SubjectDTO>> findAll(Authentication authentication,
                                                   @RequestParam(required = false) Boolean isActive,
                                                   @RequestParam(required = false) Boolean onlyWithFlashcards,
                                                   Pageable pageable) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAll(userId, isActive, pageable, onlyWithFlashcards));
    }

    @GetMapping("/with-flashcards")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<List<SubjectWithFlashcardDTO>> findAllActiveWithFlashcards(Authentication authentication) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAllActiveWithFlashcards(userId));
    }

    @GetMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<SubjectWithPeriodDTO> findById(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        SubjectWithPeriodDTO subjectWithPeriodDTO = service.findByIdWithPeriods(subjectId, userId);
        return ResponseEntity.ok(subjectWithPeriodDTO);
    }

    @PutMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<SubjectDTO> update(Authentication authentication, @PathVariable Integer subjectId, @Valid @RequestBody UpdateSubjectDTO updateSubjectDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.update(userId, subjectId, updateSubjectDTO));
    }

    @DeleteMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Activity> delete(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        service.delete(userId, subjectId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
