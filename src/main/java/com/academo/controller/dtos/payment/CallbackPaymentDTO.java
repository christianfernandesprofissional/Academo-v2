package com.academo.controller.dtos.payment;

public record CallbackPaymentDTO(
        String sucessUrl,
        boolean autoRedirect
) {
}
