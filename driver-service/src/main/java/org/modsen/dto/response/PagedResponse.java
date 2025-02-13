package org.modsen.dto.response;

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
