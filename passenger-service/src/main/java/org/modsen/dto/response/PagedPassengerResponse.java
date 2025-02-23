package org.modsen.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PagedPassengerResponse(

        int page,

        int limit,

        long totalPassengers,

        List<PassengerResponse> passengers

) {
}
