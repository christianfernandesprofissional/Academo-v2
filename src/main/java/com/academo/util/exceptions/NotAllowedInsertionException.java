package com.academo.util.exceptions;

public class NotAllowedInsertionException extends RuntimeException {
    public NotAllowedInsertionException() {
        super();
    }
    public NotAllowedInsertionException(String message) {
        super(message);
    }
}
