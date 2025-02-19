package org.modsen.dto.request;

import java.util.List;
import lombok.Builder;

@Builder
public record RideAvailableEvent(

    String rideId,

    float rating,

    List<Double> startingCoordinates,

    List<Double> endingCoordinates

) {
}
