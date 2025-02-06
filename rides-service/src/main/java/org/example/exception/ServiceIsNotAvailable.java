package org.example.exception;

public class ServiceIsNotAvailable extends RuntimeException {

    public ServiceIsNotAvailable(String message) {
        super(message);
    }

}
