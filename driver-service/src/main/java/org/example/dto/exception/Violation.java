package org.example.dto.exception;

public record Violation(String fieldName,
                        String message) {
}
