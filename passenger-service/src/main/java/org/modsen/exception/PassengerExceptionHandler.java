package org.modsen.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import org.modsen.dto.exception.ErrorResponse;
import org.modsen.dto.exception.ValidationErrorResponse;
import org.modsen.dto.exception.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PassengerExceptionHandler {

    @ExceptionHandler({
        PassengerNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(PassengerNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler({
        RepeatedDataException.class
    })
    ResponseEntity<ErrorResponse> handleRepeatedDataException(RepeatedDataException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler({
        RequestTimeoutException.class
    })
    ResponseEntity<ErrorResponse> handleBadRequestException(RequestTimeoutException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({
        ConstraintViolationException.class
    })
    ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        final List<Violation> violations = ex.getConstraintViolations().stream()
            .map(violation -> new Violation(
                violation.getPropertyPath().toString(),
                violation.getMessage()))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class
    })
    ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final List<Violation> violations = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ValidationErrorResponse(violations));
    }

}
