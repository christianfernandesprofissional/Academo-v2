package com.academo.controller;


import com.academo.controller.dtos.flashcard.CreateFlashcardDTO;
import com.academo.controller.dtos.flashcard.FlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateFlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateLevelDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.flashcard.IFlashcardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/flashcards")
public class FlashcardController {

    @Autowired
    private final IFlashcardService service;

    public FlashcardController(IFlashcardService service ){
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<Page<FlashcardDTO>> findAll(Authentication authentication, Pageable pageable) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAllByUserId(userId, pageable));
    }

    @GetMapping("/all/{subjectId}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<List<FlashcardDTO>> findAllBySubject(Authentication authentication, @PathVariable Integer subjectId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAllBySubjectId(userId, subjectId));
    }
    @GetMapping("/all/{subjectId}/{level}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<List<FlashcardDTO>> findAllBySubjectAndByLevel(Authentication authentication, @PathVariable Integer subjectId, @PathVariable String level) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAllByLevel(userId, subjectId, level));
    }
    @GetMapping("/in-group/{groupId}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<List<FlashcardDTO>> findAllByGroup(Authentication authentication,
                                                              @PathVariable Integer groupId,
                                                              @RequestParam(required = false) String level) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAllByGroupId(userId, groupId, level));
    }
    @GetMapping("/{flashcardId}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<FlashcardDTO> findById(Authentication authentication, @PathVariable Integer flashcardId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findById(userId, flashcardId));
    }

    @PostMapping
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<FlashcardDTO> create(Authentication authentication, @RequestBody @Valid CreateFlashcardDTO flashcardDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        FlashcardDTO created = service.create(userId, flashcardDTO);
        URI location = URI.create("/flashcards/" + created.id());
        return ResponseEntity.created(location).body(created);
    }
    @PutMapping("/{flashcardId}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<FlashcardDTO> update(Authentication authentication,@PathVariable Integer flashcardId, @RequestBody @Valid UpdateFlashcardDTO flashcardDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.update(userId, flashcardId, flashcardDTO));
    }
    @PatchMapping("/{flashcardId}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<FlashcardDTO> updateLeveL(Authentication authentication, @PathVariable Integer flashcardId, @RequestBody @Valid UpdateLevelDTO levelDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.updateLevel(userId, flashcardId, levelDTO));
    }
    @DeleteMapping("/{flashcardId}")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Integer flashcardId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        service.delete(userId,flashcardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
