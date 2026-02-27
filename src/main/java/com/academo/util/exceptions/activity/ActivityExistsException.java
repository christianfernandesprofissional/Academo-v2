package com.academo.util.exceptions.activity;

public class ActivityExistsException extends RuntimeException{

    public ActivityExistsException(){
        super("A atividade ja existe");
    }

    public ActivityExistsException(String message){
        super(message);
    }
}
