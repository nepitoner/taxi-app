package org.modsen.utils.validator;

import org.modsen.dto.response.RideResponse;

import java.util.UUID;

public interface RatingValidator {

    RideResponse checkRideExistenceAndPresence(UUID rideId, UUID participantId);

    void checkIfAlreadyRated(UUID participantId, UUID rideId);

    void checkRatingExistence(UUID ratingId, UUID fromId);

    void checkIfAlreadyCommented(UUID participantId);

}
