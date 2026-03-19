package com.academo.service.payment;

import com.academo.controller.dtos.payment.PaymentLinkDTO;

public interface IPaymentService {

    PaymentLinkDTO creditCardPayment(PaymentLinkDTO paymentLinkDTO);
    PaymentLinkDTO boletoPayment(PaymentLinkDTO paymentLinkDTO);
    PaymentLinkDTO pixPayment(PaymentLinkDTO paymentLinkDTO);
}
