package org.modsen.exception;

public class InvalidRideDataException extends IllegalArgumentException {

    public InvalidRideDataException(String message) {
        super(message);
    }
}
