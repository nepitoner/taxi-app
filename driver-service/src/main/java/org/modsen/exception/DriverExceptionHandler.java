package org.modsen.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import org.modsen.dto.exception.ErrorResponse;
import org.modsen.dto.exception.ValidationErrorResponse;
import org.modsen.dto.exception.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DriverExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
        DriverNotFoundException.class,
        CarNotFoundException.class,
        NoAvailableDriversException.class
    })
    public ErrorResponse handleNotFoundException(Exception exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
        RepeatedDataException.class,
        CarAlreadyTakenException.class
    })
    ErrorResponse handleRepeatedDataException(Exception exception) {
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler({
        RequestTimeoutException.class,
        ServiceIsNotAvailable.class
    })
    ErrorResponse handleBadRequestException(RequestTimeoutException exception) {
        return new ErrorResponse(HttpStatus.REQUEST_TIMEOUT, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        ConstraintViolationException.class
    })
    ValidationErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
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

}
