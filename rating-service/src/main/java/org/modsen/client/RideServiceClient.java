package org.modsen.client;

import org.modsen.dto.response.RideResponse;
import java.util.UUID;

public interface RideServiceClient {

    default RideResponse getRideById(UUID rideId, UUID participantId) {
        return RideResponse.builder()
                .rideId(rideId)
                .passengerId(UUID.fromString("4f9ee4c1-fc0e-43d7-8300-fd1d32bc0e51"))
                .driverId(participantId)
                .build();
    }

}
