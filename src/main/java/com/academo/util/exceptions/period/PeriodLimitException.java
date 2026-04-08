package com.academo.util.exceptions.period;

public class PeriodLimitException extends RuntimeException {
    public PeriodLimitException(String message) {
        super(message);
    }
    public PeriodLimitException() {
        super("Limite de períodos da matéria alcançado!");
    }
}
