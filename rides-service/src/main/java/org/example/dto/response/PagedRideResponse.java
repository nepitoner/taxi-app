package org.example.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record PagedRideResponse(

        int page,

        int limit,

        long totalRides,

        List<RideResponse> rides

) {
}
