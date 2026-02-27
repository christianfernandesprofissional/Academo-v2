package com.academo.util.exceptions.user;

public class WrongDataException extends RuntimeException {

    public WrongDataException() { super("Usuário/Email ou Senha inválidos!"); }
    public WrongDataException(String message) {
        super(message);
    }
}
