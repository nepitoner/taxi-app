package org.modsen.dto.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(

    HttpStatus status,

    String message

) {
}
