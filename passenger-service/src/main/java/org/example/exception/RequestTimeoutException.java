package org.example.exception;

import java.util.concurrent.TimeoutException;

public class RequestTimeoutException extends TimeoutException {
    public RequestTimeoutException(String message) {
        super(message);
    }
}
