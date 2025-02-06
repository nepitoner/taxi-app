package org.modsen.client;

import org.modsen.dto.response.RideResponse;

import java.util.UUID;

public interface RideServiceClient {

    default RideResponse getRideById(UUID rideId, UUID participantId) {
        return RideResponse.builder()
                .rideId(rideId)
                .passengerId(participantId)
                .driverId(UUID.randomUUID())
                .build();
    }

}
