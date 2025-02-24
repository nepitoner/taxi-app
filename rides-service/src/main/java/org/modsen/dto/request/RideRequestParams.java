package org.modsen.dto.request;

import lombok.Builder;

@Builder
public record RideRequestParams(

    int page,

    int limit,

    String sortBy,

    String sortDirection

) {
}
