package com.academo.controller;

import com.academo.controller.dtos.period.*;
import com.academo.security.authuser.AuthUser;
import com.academo.service.period.IPeriodService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/periods")
public class PeriodController {

    private final IPeriodService service;

    public PeriodController(IPeriodService service){
        this.service = service;
    }

    @GetMapping("/all/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<PeriodDTO>> findAll(Authentication auth, @PathVariable Integer subjectId, Pageable pageable){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAll(userId, subjectId, pageable));
    }

    @GetMapping("/{subjectId}/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> findById(Authentication auth, @PathVariable Integer subjectId, @PathVariable Integer periodId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findById(userId, periodId));
    }

    @PostMapping("/exam")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> create(Authentication auth, @RequestBody @Valid CreateExamDTO examDTO){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        PeriodDTO saved = service.createExam(userId, examDTO);
        URI uri = URI.create("/periods/"+saved.id());
        return ResponseEntity.created(uri).body(saved);
    }

    @PatchMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> updatePeriodsWeigth(Authentication authentication,
                                                        @PathVariable Integer subjectId,
                                                        @RequestBody @Valid UpdateWeightDTO updateWeightDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.updatePeriodsWeigth(userId, subjectId, updateWeightDTO));
    }

    @PutMapping("/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> update(Authentication auth,@PathVariable Integer periodId, @RequestBody @Valid UpdatePeriodDTO periodDTO){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        PeriodDTO updated = service.update(userId, periodId, periodDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{subjectId}/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> delete(Authentication auth, @PathVariable Integer subjectId, @PathVariable Integer periodId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        service.delete(userId, subjectId, periodId);
        return ResponseEntity.noContent().build();
    }
}
