package com.academo.util.exceptions.period;

public class PeriodAlreadyExistsException extends RuntimeException {
    public PeriodAlreadyExistsException(String message) {
        super(message);
    }
    public PeriodAlreadyExistsException() {
        super("O período já existe nesta matéria");
    }
}
