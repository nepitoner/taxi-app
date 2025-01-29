package org.example.dto.exception;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violationList) {
}
