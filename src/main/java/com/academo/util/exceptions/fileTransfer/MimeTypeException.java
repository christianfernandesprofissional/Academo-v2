package com.academo.util.exceptions.fileTransfer;

public class MimeTypeException extends RuntimeException {
    public MimeTypeException(String message) {
        super(message);
    }

    public MimeTypeException() {
        super("Tipo de arquivo não suportado!");
    }
}
