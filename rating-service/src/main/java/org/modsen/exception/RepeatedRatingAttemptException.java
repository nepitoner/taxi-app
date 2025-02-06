package org.modsen.exception;

public class RepeatedRatingAttemptException extends IllegalArgumentException {

    public RepeatedRatingAttemptException(String message) {
        super(message);
    }

}
