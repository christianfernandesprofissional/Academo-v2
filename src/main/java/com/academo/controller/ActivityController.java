package com.academo.controller;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.model.Activity;
import com.academo.security.authuser.AuthUser;
import com.academo.service.activity.IActivityService;
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
@RequestMapping("/activities")
public class ActivityController {

    private final IActivityService activityService;

    public ActivityController(IActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<ActivityDTO>> findAll(Authentication authentication,
                                                    @RequestParam(required = false, defaultValue = "false") boolean onlyFuture,
                                                    Pageable pageable) {
       Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
       return ResponseEntity.ok(activityService.findAll(userId, onlyFuture, pageable));
    }

    @GetMapping("/{activityId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityDTO> findById(Authentication authentication, @PathVariable Integer activityId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.findById(userId, activityId));
    }

    @GetMapping("/by-period/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<ActivityDTO>> findAllByPeriod(Authentication authentication,
                                                            @PathVariable Integer periodId,
                                                            @RequestParam(required = false) List<String> activityTypeNames,
                                                            Pageable pageable) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.findAllByPeriodId(userId, periodId, activityTypeNames, pageable));
    }


    @GetMapping("/by-subject/{subjectId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Page<ActivityDTO>> findAllBySubject(Authentication authentication, @PathVariable Integer subjectId, Pageable pageable) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.findAllBySubjectId(userId, subjectId, pageable));
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityDTO> create(Authentication authentication, @RequestBody @Valid SaveActivityDTO activityDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityDTO created = activityService.create(userId, activityDTO);
        URI location = URI.create("/activities/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{activityId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityDTO> update(Authentication authentication,@PathVariable Integer activityId, @RequestBody @Valid SaveActivityDTO activityDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityService.update(userId, activityId, activityDTO));
    }

    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Integer activityId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        activityService.delete(userId,activityId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
