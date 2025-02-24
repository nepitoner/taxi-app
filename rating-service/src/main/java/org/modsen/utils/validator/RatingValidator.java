package org.modsen.utils.validator;

import java.util.UUID;
import org.modsen.dto.response.RideResponse;

public interface RatingValidator {

    RideResponse checkRideExistenceAndPresence(UUID rideId, UUID participantId);

    void checkIfAlreadyRated(UUID participantId, UUID rideId);

    void checkRatingExistence(UUID ratingId, UUID fromId);

    void checkIfAlreadyCommented(UUID participantId);

}
