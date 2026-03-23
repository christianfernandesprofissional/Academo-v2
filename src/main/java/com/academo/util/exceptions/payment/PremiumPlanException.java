package com.academo.util.exceptions.payment;

public class PremiumPlanException extends RuntimeException {

    public PremiumPlanException() {
        super("Erro ao adquirir plano Premium");
    }
    public PremiumPlanException(String message) {
        super(message);
    }
}
