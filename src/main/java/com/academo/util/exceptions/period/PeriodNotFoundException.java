package com.academo.util.exceptions.period;

public class PeriodNotFoundException extends RuntimeException {
    public PeriodNotFoundException(String message) {
        super(message);
    }
    public PeriodNotFoundException() {
        super("Periodo não encontrado!");
    }
}
