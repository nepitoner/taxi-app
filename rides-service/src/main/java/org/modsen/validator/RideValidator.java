package org.modsen.validator;

import java.util.UUID;
import org.modsen.dto.request.RideStatusRequest;

public interface RideValidator {

    void checkExistence(UUID rideId);

    void checkStatusProcessing(RideStatusRequest request, UUID rideId);

}
