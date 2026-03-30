package com.academo.service.payment;

import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentOptionsDTO;

import java.util.Map;

public interface IPaymentService {

    PaymentLinkDTO createPaymentLink(Integer userId, PaymentOptionsDTO paymentOptionsDTO);
    void receivePayment(Map<String, Object> body);
}
