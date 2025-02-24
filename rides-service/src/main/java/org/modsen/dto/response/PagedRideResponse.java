package org.modsen.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PagedRideResponse(

    int page,

    int limit,

    long totalRides,

    List<RideResponse> rides

) {
}
