package org.example.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.RideStatusRequest;
import org.example.entity.Ride;
import org.example.entity.RideStatus;
import org.example.exception.RideNotFoundException;
import org.example.exception.RideStatusProcessingException;
import org.example.repository.RideRepository;
import org.example.validator.RideValidator;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.example.utils.constant.ExceptionConstant.INVALID_RIDE_STATUS_CHANGE_MESSAGE;
import static org.example.utils.constant.ExceptionConstant.INVALID_RIDE_STATUS_MESSAGE;
import static org.example.utils.constant.ExceptionConstant.RIDE_NOT_FOUND_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RideValidatorImpl implements RideValidator {

    private final RideRepository rideRepository;

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
        throwIfCurrentStatusIsCompleted(currentStatus);
        throwIfCurrentStatusIsCancelled(currentStatus);
        throwIfNewStatusIsCompletedWithoutSequence(currentStatus, newStatus);
        throwIfInvalidStatusSequence(currentStatus, newStatus);
    }

    private static void throwIfNewStatusIsCompletedWithoutSequence(RideStatus currentStatus, RideStatus newStatus) {
        if (newStatus == RideStatus.COMPLETED && currentStatus != RideStatus.ON_WAY_TO_DESTINATION) {
            throw new RideStatusProcessingException(INVALID_RIDE_STATUS_MESSAGE.formatted(newStatus));
        }
    }

    private static void throwIfNewStatusIsCreated(RideStatus newStatus) {
        if (newStatus == RideStatus.CREATED) {
            throw new RideStatusProcessingException(INVALID_RIDE_STATUS_MESSAGE.formatted(RideStatus.CREATED));
        }
    }

    private static void throwIfCurrentStatusIsCancelled(RideStatus currentStatus) {
        if (currentStatus == RideStatus.CANCELED) {
            throw new RideStatusProcessingException(INVALID_RIDE_STATUS_CHANGE_MESSAGE.formatted(currentStatus));
        }
    }

    private static void throwIfCurrentStatusIsCompleted(RideStatus currentStatus) {
        if (currentStatus == RideStatus.COMPLETED) {
            throw new RideStatusProcessingException(INVALID_RIDE_STATUS_CHANGE_MESSAGE.formatted(currentStatus));
        }
    }

    private static void throwIfInvalidStatusSequence(RideStatus currentStatus, RideStatus newStatus) {
        if (currentStatus.getCode() + 1 != newStatus.getCode() && newStatus != RideStatus.COMPLETED) {
            throw new RideStatusProcessingException(INVALID_RIDE_STATUS_MESSAGE.formatted(newStatus));
        }
    }

}
