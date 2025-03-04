package org.modsen.dto.exception;

public record Violation(

    String fieldName,

    String message

) {
}
