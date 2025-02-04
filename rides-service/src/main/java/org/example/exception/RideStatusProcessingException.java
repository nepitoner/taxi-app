package org.example.exception;

public class RideStatusProcessingException extends IllegalArgumentException {
    public RideStatusProcessingException(String message) {
        super(message);
    }
}
