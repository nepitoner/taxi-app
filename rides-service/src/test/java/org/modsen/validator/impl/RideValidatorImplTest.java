package org.modsen.validator.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.modsen.entity.RideStatus.ACCEPTED;
import static org.modsen.entity.RideStatus.COMPLETED;
import static org.modsen.entity.RideStatus.CREATED;
import static org.modsen.util.TestUtil.ride;
import static org.modsen.util.constant.ExceptionConstant.INVALID_RIDE_STATUS_CHANGE_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.INVALID_RIDE_STATUS_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.RIDE_NOT_FOUND_MESSAGE;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.dto.request.RideStatusRequest;
import org.modsen.entity.Ride;
import org.modsen.entity.RideStatus;
import org.modsen.exception.RideNotFoundException;
import org.modsen.exception.RideStatusProcessingException;
import org.modsen.repository.RideRepository;

@ExtendWith(MockitoExtension.class)
class RideValidatorImplTest {

    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private RideValidatorImpl rideValidator;

    private UUID rideId;
    private Ride ride;

    @BeforeEach
    void setUp() {
        rideId = UUID.randomUUID();
        ride = ride(rideId, UUID.randomUUID(), UUID.randomUUID(), CREATED);
    }

    @Test
    @DisplayName("Test checking existence")
    void testCheckExistence() {
        when(rideRepository.existsById(rideId)).thenReturn(true);

        rideValidator.checkExistence(rideId);

        verify(rideRepository).existsById(rideId);
    }

    @Test
    @DisplayName("Test checking existence but ride does not exist")
    void testCheckExistence_NotFound() {
        when(rideRepository.existsById(rideId)).thenReturn(false);

        assertThatThrownBy(() -> rideValidator.checkExistence(rideId))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage(RIDE_NOT_FOUND_MESSAGE.formatted(rideId));

        verify(rideRepository).existsById(rideId);
    }

    @Test
    @DisplayName("Test checking status processing")
    void testCheckStatusProcessing() {
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        RideStatusRequest request = new RideStatusRequest("ACCEPTED");
        rideValidator.checkStatusProcessing(request, rideId);

        verify(rideRepository).findById(rideId);
    }

    @Test
    @DisplayName("Test checking status processing but new status is CREATED")
    void testCheckStatusProcessing_NewStatusCreated() {
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        RideStatusRequest request = new RideStatusRequest("CREATED");

        assertThatThrownBy(() -> rideValidator.checkStatusProcessing(request, rideId))
            .isInstanceOf(RideStatusProcessingException.class)
            .hasMessage(INVALID_RIDE_STATUS_MESSAGE.formatted(RideStatus.CREATED));

        verify(rideRepository).findById(rideId);
    }

    @Test
    @DisplayName("Test checking status processing but current status is COMPLETED")
    void testCheckStatusProcessing_CurrentStatusCompleted() {
        ride.setRideStatus(COMPLETED);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        RideStatusRequest request = new RideStatusRequest("ON_WAY_TO_DESTINATION");

        assertThatThrownBy(() -> rideValidator.checkStatusProcessing(request, rideId))
            .isInstanceOf(RideStatusProcessingException.class)
            .hasMessage(INVALID_RIDE_STATUS_CHANGE_MESSAGE.formatted(COMPLETED));

        verify(rideRepository).findById(rideId);
    }

    @Test
    @DisplayName("Test checking status processing but current status is CANCELED")
    void testCheckStatusProcessing_CurrentStatusCanceled() {
        ride.setRideStatus(RideStatus.CANCELED);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        RideStatusRequest request = new RideStatusRequest("ON_WAY_TO_DESTINATION");

        assertThatThrownBy(() -> rideValidator.checkStatusProcessing(request, rideId))
            .isInstanceOf(RideStatusProcessingException.class)
            .hasMessage(INVALID_RIDE_STATUS_CHANGE_MESSAGE.formatted(RideStatus.CANCELED));

        verify(rideRepository).findById(rideId);
    }

    @Test
    @DisplayName("Test checking status processing but status sequence is invalid")
    void testCheckStatusProcessing_InvalidStatusSequence() {
        ride.setRideStatus(ACCEPTED);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        RideStatusRequest request = new RideStatusRequest("COMPLETED");

        assertThatThrownBy(() -> rideValidator.checkStatusProcessing(request, rideId))
            .isInstanceOf(RideStatusProcessingException.class)
            .hasMessage(INVALID_RIDE_STATUS_MESSAGE.formatted(COMPLETED));

        verify(rideRepository).findById(rideId);
    }

}