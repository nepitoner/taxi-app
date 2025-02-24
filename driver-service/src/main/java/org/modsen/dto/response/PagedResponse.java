package org.modsen.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PagedResponse<T>(

    int page,

    int limit,

    long totalAmount,

    List<T> elements

) {
}
