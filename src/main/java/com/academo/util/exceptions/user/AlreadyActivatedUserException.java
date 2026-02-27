package com.academo.util.exceptions.user;

public class AlreadyActivatedUserException extends RuntimeException {
    public AlreadyActivatedUserException(String message) {
        super(message);
    }
}
