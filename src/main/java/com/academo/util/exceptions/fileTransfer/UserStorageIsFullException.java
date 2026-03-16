package com.academo.util.exceptions.fileTransfer;

public class UserStorageIsFullException extends RuntimeException {

    public UserStorageIsFullException(String message) {
        super(message);
    }

    public UserStorageIsFullException() {
        super("Limite de armazenamento atingido. Libere espaço para novos arquivos.");
    }
}
