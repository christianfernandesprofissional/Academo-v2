package com.academo.service.payment.asaas;

import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.service.payment.IPaymentService;

public class AsaasPaymentService implements IPaymentService {


    @Override
    public PaymentLinkDTO creditCardPayment(PaymentLinkDTO paymentLinkDTO) {
        return null;
    }

    @Override
    public PaymentLinkDTO boletoPayment(PaymentLinkDTO paymentLinkDTO) {
        return null;
    }

    @Override
    public PaymentLinkDTO pixPayment(PaymentLinkDTO paymentLinkDTO) {
        return null;
    }
}
