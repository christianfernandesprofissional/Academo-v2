package com.academo.util.exceptions.payment.history;

public class PaymentHistoryNotFoundException extends RuntimeException {

    public PaymentHistoryNotFoundException() {
        super("Histórico de Pagamento não encontrado");
    }
    public PaymentHistoryNotFoundException(String message) {
        super(message);
    }
}
