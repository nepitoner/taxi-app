package org.modsen.exception;

public class ParticipantNotFoundException extends RuntimeException {

    public ParticipantNotFoundException(String message) {
        super(message);
    }
}
