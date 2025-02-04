package org.example.exception;

public class IncorrectStatusException extends IllegalArgumentException {
    public IncorrectStatusException(String message) {
        super(message);
    }
}
