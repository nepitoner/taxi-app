package org.modsen.validator.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.client.RideServiceClient;
import org.modsen.dto.response.RideResponse;
import org.modsen.exception.ParticipantNotFoundException;
import org.modsen.exception.RatingNotFoundException;
import org.modsen.exception.RepeatedRatingAttemptException;
import org.modsen.repository.RatingRepository;

@ExtendWith(MockitoExtension.class)
public class RatingValidatorImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RideServiceClient rideServiceClient;

    @InjectMocks
    private RatingValidatorImpl ratingValidator;

    private UUID rideId;
    private UUID participantId;

    @BeforeEach
    void setUp() {
        rideId = UUID.randomUUID();
        participantId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Test check ride existence and presence")
    void testCheckRideExistenceAndPresence() {
        RideResponse rideResponse = new RideResponse(rideId, participantId, UUID.randomUUID());
        when(rideServiceClient.getRideById(rideId)).thenReturn(rideResponse);

        RideResponse result = ratingValidator.checkRideExistenceAndPresence(rideId, participantId);

        assertThat(result).isEqualTo(rideResponse);
        verify(rideServiceClient).getRideById(rideId);
    }

    @Test
    @DisplayName("Test check ride existence and presence for invalid participant")
    void testCheckRideExistenceAndPresenceInvalidParticipant() {
        RideResponse rideResponse = new RideResponse(rideId, UUID.randomUUID(), UUID.randomUUID());
        when(rideServiceClient.getRideById(rideId)).thenReturn(rideResponse);

        assertThatThrownBy(() -> ratingValidator.checkRideExistenceAndPresence(rideId, participantId))
            .isInstanceOf(ParticipantNotFoundException.class)
            .hasMessageContaining(participantId.toString());

        verify(rideServiceClient).getRideById(rideId);
    }

    @Test
    @DisplayName("Test check if already rated")
    void testCheckIfAlreadyRated() {
        when(ratingRepository.existsByFromIdAndRideId(participantId, rideId)).thenReturn(false);

        assertThatCode(() -> ratingValidator.checkIfAlreadyRated(participantId, rideId))
            .doesNotThrowAnyException();

        verify(ratingRepository).existsByFromIdAndRideId(participantId, rideId);
    }

    @Test
    @DisplayName("Test check if already rated but already exists")
    void testCheckIfAlreadyRatedButExists() {
        when(ratingRepository.existsByFromIdAndRideId(participantId, rideId)).thenReturn(true);

        assertThatThrownBy(() -> ratingValidator.checkIfAlreadyRated(participantId, rideId))
            .isInstanceOf(RepeatedRatingAttemptException.class)
            .hasMessageContaining(participantId.toString());

        verify(ratingRepository).existsByFromIdAndRideId(participantId, rideId);
    }

    @Test
    @DisplayName("Test check rating existence")
    void testCheckRatingExistence() {
        when(ratingRepository.existsByRatingIdAndFromId(rideId, participantId)).thenReturn(true);

        assertThatCode(() -> ratingValidator.checkRatingExistence(rideId, participantId))
            .doesNotThrowAnyException();

        verify(ratingRepository).existsByRatingIdAndFromId(rideId, participantId);
    }

    @Test
    @DisplayName("Test check rating existence but doesn't exist")
    void testCheckRatingExistenceButDoesNotExist() {
        when(ratingRepository.existsByRatingIdAndFromId(rideId, participantId)).thenReturn(false);

        assertThatThrownBy(() -> ratingValidator.checkRatingExistence(rideId, participantId))
            .isInstanceOf(RatingNotFoundException.class)
            .hasMessageContaining(participantId.toString());

        verify(ratingRepository).existsByRatingIdAndFromId(rideId, participantId);
    }

    @Test
    @DisplayName("Test check if already commented")
    void testCheckIfAlreadyCommented() {
        when(ratingRepository.existsByRatingIdAndCommentNotNull(rideId)).thenReturn(Optional.of(false));

        assertThatCode(() -> ratingValidator.checkIfAlreadyCommented(rideId))
            .doesNotThrowAnyException();

        verify(ratingRepository).existsByRatingIdAndCommentNotNull(rideId);
    }

    @Test
    @DisplayName("Test check if already commented but already exists")
    void testCheckIfAlreadyCommentedButExists() {
        when(ratingRepository.existsByRatingIdAndCommentNotNull(rideId)).thenReturn(Optional.of(true));

        assertThatThrownBy(() -> ratingValidator.checkIfAlreadyCommented(rideId))
            .isInstanceOf(RepeatedRatingAttemptException.class)
            .hasMessageContaining(rideId.toString());

        verify(ratingRepository).existsByRatingIdAndCommentNotNull(rideId);
    }

}