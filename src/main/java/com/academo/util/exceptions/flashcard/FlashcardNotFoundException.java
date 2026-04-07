package com.academo.util.exceptions.flashcard;

public class FlashcardNotFoundException extends RuntimeException {
    public FlashcardNotFoundException() {
        super("Flashcard não encontrado!");
    }
    public FlashcardNotFoundException(String message) {
        super(message);
    }
}
