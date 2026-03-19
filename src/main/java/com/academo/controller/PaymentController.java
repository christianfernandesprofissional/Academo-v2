package com.academo.controller;

import com.academo.controller.dtos.payment.PaymentLinkDTO;
import org.hibernate.validator.internal.constraintvalidators.bv.time.past.PastValidatorForYear;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @PostMapping("/credit-card")
    public ResponseEntity<PaymentLinkDTO> creditCardPayment(@RequestBody PaymentLinkDTO paymentLinkDTO) {
        return null;
    }

    @PostMapping("/boleto")
    public ResponseEntity<PaymentLinkDTO> boletoPayment(@RequestBody PaymentLinkDTO paymentLinkDTO) {
        return null;
    }

    @PostMapping("/pix")
    public ResponseEntity<PaymentLinkDTO> pixPayment(@RequestBody PaymentLinkDTO paymentLinkDTO) {
        return null;
    }

}
