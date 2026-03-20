package com.academo.service.payment;

import com.academo.controller.dtos.payment.GetPaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentOptionsData;

public interface IPaymentService {

    PaymentLinkDTO createPaymentLink(PaymentOptionsData paymentOptionsData);
}
