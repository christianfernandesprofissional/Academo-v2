package com.academo.controller;

import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.model.Period;
import com.academo.model.User;
import com.academo.security.authuser.AuthUser;
import com.academo.service.period.IPeriodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/periods")
public class PeriodController {

    private final IPeriodService service;

    public PeriodController(IPeriodService service){
        this.service = service;
    }


    @GetMapping("/all/{subjectId}")
    public ResponseEntity<List<PeriodDTO>> findAll(Authentication auth, @PathVariable Integer subjectId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(service.findAll(userId, subjectId));
    }

    @GetMapping("/{periodId}")
    public ResponseEntity<Period> findById(Authentication auth, @PathVariable Integer subjectId, @PathVariable Integer periodId){
        Integer userId = ((AuthUser)auth.getPrincipal()).getUser().getId();
        service.findById(userId, periodId);
        return null;
    }

    @GetMapping
    public ResponseEntity<Period> create(){
        return null;
    }

    @GetMapping
    public ResponseEntity<Period> update(){
        return null;
    }

    @GetMapping
    public ResponseEntity<Period> delete(){
        return null;
    }
}
