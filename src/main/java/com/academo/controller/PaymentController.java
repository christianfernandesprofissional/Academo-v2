package com.academo.controller;

import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentOptionsDTO;
import com.academo.service.payment.IPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FREE', 'PREMIUM')")
    public ResponseEntity<PaymentLinkDTO> createPaymentLink(@RequestBody PaymentOptionsDTO paymentOptionsDTO) {
        return ResponseEntity.ok(paymentService.createPaymentLink(paymentOptionsDTO));
    }



}
