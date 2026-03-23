package com.academo.controller;

import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.controller.dtos.period.UpdatePeriodDTO;
import com.academo.model.Period;
import com.academo.model.User;
import com.academo.security.authuser.AuthUser;
import com.academo.service.period.IPeriodService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
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


    @GetMapping("/all/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<List<PeriodDTO>> findAll(Authentication auth, @PathVariable Integer subjectId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAll(userId, subjectId));
    }

    @GetMapping("/{subjectId}/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> findById(Authentication auth, @PathVariable Integer subjectId, @PathVariable Integer periodId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findById(userId, periodId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> create(Authentication auth, @RequestBody SavePeriodDTO periodDTO){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        PeriodDTO saved = service.create(userId, periodDTO);
        URI uri = URI.create("/periods/"+saved.id());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PeriodDTO> update(Authentication auth, @RequestBody UpdatePeriodDTO periodDTO){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        PeriodDTO updated = service.update(userId, periodDTO);
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
