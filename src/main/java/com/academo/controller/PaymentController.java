package com.academo.controller;

import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentOptionsDTO;
import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.payment.IPaymentService;
import com.academo.service.payment.history.IPaymentHistoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentService paymentService;
    private final IPaymentHistoryService paymentHistoryService;

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(IPaymentService paymentService, IPaymentHistoryService paymentHistoryService) {
        this.paymentService = paymentService;
        this.paymentHistoryService = paymentHistoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PaymentLinkDTO> createPaymentLink(Authentication authentication, @RequestBody @Valid PaymentOptionsDTO paymentOptionsDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(paymentService.createPaymentLink(userId, paymentOptionsDTO));
    }

    @PostMapping(value = "/receive", consumes = "application/json")
    public ResponseEntity<Void> receivePayment(@RequestBody Map<String, Object> body) {
        paymentService.receivePayment(body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<List<PaymentHistoryDTO>> findAll(Authentication authentication) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(paymentHistoryService.findAll(userId));
    }

    @PostMapping(value = "/cancel")
    @PreAuthorize("hasRole('PREMIUM')")
    public ResponseEntity<Void> cancelPlan(Authentication authentication) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        paymentHistoryService.cancelPlan(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }





}
