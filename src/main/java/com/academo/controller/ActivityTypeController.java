package com.academo.controller;

import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.activityType.UpdateActivityTypeDTO;
import com.academo.controller.dtos.activityType.UpdateActivityTypeWeightDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.activityType.IActivityTypeService;
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
@RequestMapping("/activity-types")
public class ActivityTypeController {

    private final IActivityTypeService activityTypeService;

    public ActivityTypeController(IActivityTypeService activityService){
        this.activityTypeService = activityService;
    }

    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    @GetMapping("/all/{periodId}")
    public ResponseEntity<Page<ActivityTypeDTO>> findAll(Authentication authentication, @PathVariable Integer periodId, Pageable pageable) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(activityTypeService.findAll(userId, periodId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityTypeDTO> findById(Authentication authentication, @PathVariable Integer id) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityTypeDTO activityTypeDTO = activityTypeService.findDTO(id, userId);
        return ResponseEntity.ok(activityTypeDTO);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityTypeDTO> create(Authentication authentication, @RequestBody @Valid SaveActivityTypeDTO activityTypeDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityTypeDTO createdActivityType = activityTypeService.create(userId, activityTypeDTO);
        URI uri = URI.create("/activity-types/" + createdActivityType.id());
        return ResponseEntity.created(uri).body(createdActivityType);
    }

    @PatchMapping("/{periodId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Void> updatePeriodWeights(Authentication authentication, @PathVariable Integer periodId, @RequestBody @Valid UpdateActivityTypeWeightDTO weightsDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        activityTypeService.updateWeightsByPeriod(userId, periodId, weightsDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<ActivityTypeDTO> update(Authentication authentication,@PathVariable Integer id, @RequestBody @Valid UpdateActivityTypeDTO activityTypeDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        ActivityTypeDTO updated = activityTypeService.update(userId, id, activityTypeDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{activityTypeId}")
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Integer activityTypeId) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        activityTypeService.delete(userId, activityTypeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
