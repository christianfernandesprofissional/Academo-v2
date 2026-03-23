package com.academo.controller;

import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentOptionsDTO;
import com.academo.security.authuser.AuthUser;
import com.academo.service.payment.IPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentService paymentService;

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PaymentLinkDTO> createPaymentLink(Authentication authentication, @RequestBody PaymentOptionsDTO paymentOptionsDTO) {
        Integer userId = ((AuthUser) authentication.getPrincipal()).getUser().getId();
        return ResponseEntity.ok(paymentService.createPaymentLink(userId, paymentOptionsDTO));
    }





}
