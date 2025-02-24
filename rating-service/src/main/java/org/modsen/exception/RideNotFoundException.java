package org.modsen.exception;

public class RideNotFoundException extends IllegalArgumentException {

    public RideNotFoundException(String message) {
        super(message);
    }
}
