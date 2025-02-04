package org.example.exception;

public class DistanceCalculationException extends IllegalArgumentException {
    public DistanceCalculationException(String message) {
        super(message);
    }
}
