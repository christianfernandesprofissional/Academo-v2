package com.academo.util.exceptions.fileTransfer;

public class FileSizeException extends RuntimeException {
    public FileSizeException(String message) {
        super(message);
    }

    public FileSizeException() {
        super("Tamanho máximo excedido. O arquivo deve ter no máximo 15MB");
    }
}
