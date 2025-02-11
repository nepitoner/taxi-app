package org.modsen.exception;

public class CarAlreadyTakenException extends RuntimeException {

    public CarAlreadyTakenException(String message) {
        super(message);
    }
}
