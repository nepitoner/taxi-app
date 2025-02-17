package org.modsen.validator;

import org.modsen.dto.request.RideStatusRequest;

import java.util.UUID;

public interface RideValidator {

    void checkExistence(UUID rideId);

    void checkStatusProcessing(RideStatusRequest request, UUID rideId);

}
