package com.academo.service.payment;

import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentOptionsDTO;

public interface IPaymentService {

    PaymentLinkDTO createPaymentLink(PaymentOptionsDTO paymentOptionsDTO);
}
