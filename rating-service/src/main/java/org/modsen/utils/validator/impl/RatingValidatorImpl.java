package org.modsen.utils.validator.impl;

import static org.modsen.utils.constant.ExceptionConstant.INVALID_RIDE_DATA_MESSAGE;
import static org.modsen.utils.constant.ExceptionConstant.RATING_NOT_FOUND_MESSAGE;
import static org.modsen.utils.constant.ExceptionConstant.REPEATED_ATTEMPT_MESSAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.client.RideServiceClient;
import org.modsen.dto.response.RideResponse;
import org.modsen.exception.InvalidRideDataException;
import org.modsen.exception.RatingNotFoundException;
import org.modsen.exception.RepeatedRatingAttemptException;
import org.modsen.repository.RatingRepository;
import org.modsen.utils.validator.RatingValidator;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingValidatorImpl implements RatingValidator {

    private final RatingRepository ratingRepository;

    private final RideServiceClient rideServiceClient;

    @Override
    public RideResponse checkRideExistenceAndPresence(UUID rideId, UUID fromId) {
        RideResponse rideResponse = rideServiceClient.getRideById(rideId, fromId);
        if (!rideResponse.passengerId().equals(fromId) &&
                !rideResponse.driverId().equals(fromId)) {
            throw new InvalidRideDataException(INVALID_RIDE_DATA_MESSAGE);
        }
        return rideResponse;
    }

    @Override
    public void checkIfAlreadyRated(UUID participantId, UUID rideId) {
        if (ratingRepository.existsByFromIdAndRideId(participantId, rideId)) {
            throw new RepeatedRatingAttemptException(
                    REPEATED_ATTEMPT_MESSAGE.formatted(participantId , "rate"));
        }
    }

    @Override
    public void checkRatingExistence(UUID ratingId, UUID fromId) {
        if (!ratingRepository.existsByRatingIdAndFromId(ratingId, fromId)) {
            throw new RatingNotFoundException(RATING_NOT_FOUND_MESSAGE.formatted(fromId));
        }
    }

    @Override
    public void checkIfAlreadyCommented(UUID ratingId) {
        if (ratingRepository.existsByRatingIdAndCommentNotNull(ratingId).get()) {
            throw new RepeatedRatingAttemptException(
                    REPEATED_ATTEMPT_MESSAGE.formatted(ratingId, "comment"));
        }
    }

}
