package org.example.exception;

public class RepeatedDataException extends IllegalArgumentException {
    public RepeatedDataException(String message) {
        super(message);
    }
}
