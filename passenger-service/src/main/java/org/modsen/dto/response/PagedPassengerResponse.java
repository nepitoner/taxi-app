package org.modsen.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PagedPassengerResponse(

    int page,

    int limit,

    long totalPassengers,

    List<PassengerResponse> passengers

) {
}
