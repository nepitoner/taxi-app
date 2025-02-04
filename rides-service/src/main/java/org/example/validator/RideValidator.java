package org.example.validator;

import org.example.dto.request.RideStatusRequest;

import java.util.UUID;

public interface RideValidator {

    void checkExistence(UUID rideId);

    void checkStatusProcessing(RideStatusRequest request, UUID rideId);
}
