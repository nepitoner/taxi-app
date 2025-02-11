package org.modsen.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PagedResponse<T> (
        int page,
        int limit,
        long totalAmount,
        List<T> elements
) {
}
