package org.modsen.validator.impl;

import static org.modsen.utils.constant.ExceptionConstant.INVALID_RIDE_STATUS_CHANGE_MESSAGE;
import static org.modsen.utils.constant.ExceptionConstant.INVALID_RIDE_STATUS_MESSAGE;
import static org.modsen.utils.constant.ExceptionConstant.RIDE_NOT_FOUND_MESSAGE;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.request.RideStatusRequest;
import org.modsen.entity.Ride;
import org.modsen.entity.RideStatus;
import org.modsen.exception.RideNotFoundException;
import org.modsen.exception.RideStatusProcessingException;
import org.modsen.repository.RideRepository;
import org.modsen.validator.RideValidator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RideValidatorImpl implements RideValidator {

    private final RideRepository rideRepository;

    private static void throwIfNewStatusIsCreated(RideStatus newStatus) {
        if (newStatus.equals(RideStatus.CREATED)) {
            throw new RideStatusProcessingException(INVALID_RIDE_STATUS_MESSAGE.formatted(newStatus));
        }
    }

    private static void throwIfCurrentStatusIsCancelledOrCompleted(RideStatus currentStatus) {
        if (currentStatus.equals(RideStatus.COMPLETED) || currentStatus.equals(RideStatus.CANCELED)) {
            throw new RideStatusProcessingException(INVALID_RIDE_STATUS_CHANGE_MESSAGE.formatted(currentStatus));
        }
    }

    private static void throwIfInvalidStatusSequence(RideStatus currentStatus, RideStatus newStatus) {
        if ((newStatus.equals(RideStatus.COMPLETED) && !currentStatus.equals(RideStatus.ON_WAY_TO_DESTINATION)) ||
            (currentStatus.getCode() + 100 != newStatus.getCode() && !newStatus.equals(RideStatus.COMPLETED))) {
            throw new RideStatusProcessingException(INVALID_RIDE_STATUS_MESSAGE.formatted(newStatus));
        }
    }

    @Override
    public void checkExistence(UUID rideId) {
        if (!rideRepository.existsById(rideId)) {
            throw new RideNotFoundException(RIDE_NOT_FOUND_MESSAGE.formatted(rideId));
        }
    }

    @Override
    public void checkStatusProcessing(RideStatusRequest request, UUID rideId) {
        log.info("Ride Validator. Searching for a ride with id, {}", rideId);
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_MESSAGE.formatted(rideId)));
        log.info("Ride Validator. Ride with id exists, {}", ride.getRideId());

        RideStatus currentStatus = ride.getRideStatus();
        RideStatus newStatus = RideStatus.valueOf(request.rideStatus());

        throwIfNewStatusIsCreated(newStatus);
        throwIfCurrentStatusIsCancelledOrCompleted(currentStatus);
        throwIfInvalidStatusSequence(currentStatus, newStatus);
    }

}
