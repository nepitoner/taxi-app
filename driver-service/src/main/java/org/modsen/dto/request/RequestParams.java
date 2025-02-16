package org.modsen.dto.request;

import lombok.Builder;

@Builder
public record RequestParams(

        int page,

        int limit,

        String sortBy,

        String sortDirection

) {
}
