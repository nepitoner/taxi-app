package org.modsen.exception;

import jakarta.validation.ConstraintViolationException;
import org.modsen.dto.exception.ErrorResponse;
import org.modsen.dto.exception.ValidationErrorResponse;
import org.modsen.dto.exception.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RideExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            RideNotFoundException.class
    })
    public ErrorResponse handleRideNotFound(RideNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            DistanceCalculationException.class,
            RideStatusProcessingException.class,
            IncorrectStatusException.class,
            IllegalArgumentException.class
    })
    public ErrorResponse handleIllegalArgumentException(Exception exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            ConstraintViolationException.class
    })
    public ValidationErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        final List<Violation> violations = ex.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    ValidationErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final List<Violation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            ServiceIsNotAvailable.class,
            Exception.class
    })
    ErrorResponse handleOtherException(Exception exception) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

}
