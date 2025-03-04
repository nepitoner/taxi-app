package org.modsen.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.modsen.util.TestUtil.rateResponse;
import static org.modsen.util.TestUtil.rating;
import static org.modsen.util.TestUtil.ratingRequest;
import static org.modsen.util.TestUtil.ratingResponse;
import static org.modsen.util.TestUtil.requestParams;
import static org.modsen.util.TestUtil.rideComment;
import static org.modsen.util.constant.ExceptionConstant.RATING_NOT_FOUND_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_ATTEMPT_MESSAGE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.config.properties.RatingServiceProperties;
import org.modsen.dto.request.RatingRequest;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.request.RideCommentRequest;
import org.modsen.dto.response.PagedRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.dto.response.RatingResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.entity.Rating;
import org.modsen.entity.RatingChangeEvent;
import org.modsen.exception.RatingNotFoundException;
import org.modsen.exception.RepeatedRatingAttemptException;
import org.modsen.exception.RideNotFoundException;
import org.modsen.mapper.RatingMapper;
import org.modsen.repository.RatingChangeEventRepository;
import org.modsen.repository.RatingRepository;
import org.modsen.validator.RatingValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingValidator ratingValidator;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingServiceProperties properties;

    @Mock
    private RatingChangeEventRepository ratingChangeEventRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private UUID ratingId;
    private UUID rideId;
    private UUID toId;
    private UUID fromId;
    private RatingRequest ratingRequest;
    private RatingResponse ratingResponse;
    private RateResponse rateResponse;
    private Rating rating;
    private RideResponse rideResponse;
    private RequestParams requestParams;
    private RideCommentRequest commentRequest;

    @BeforeEach
    void setUp() {
        ratingId = UUID.randomUUID();
        rideId = UUID.randomUUID();
        fromId = UUID.randomUUID();
        toId = UUID.randomUUID();
        ratingRequest = ratingRequest(rideId);
        ratingResponse = ratingResponse(ratingId, fromId, toId, rideId);
        rateResponse = rateResponse(toId);
        rating = rating(ratingId, fromId, toId, rideId);
        rideResponse = new RideResponse(rideId, fromId, toId);
        requestParams = requestParams();
        commentRequest = rideComment(ratingId);
    }

    @Test
    @DisplayName("Test getting all ratings")
    void testGetAllRatings() {
        PagedRatingResponse pagedRatingResponse =
            new PagedRatingResponse(0, 10, 1, List.of(ratingResponse));
        Page<Rating> ratingPage = mock(Page.class);
        when(ratingRepository.findAll(any(Pageable.class))).thenReturn(ratingPage);
        when(ratingMapper.mapPageEntityToPagedDto(anyInt(), anyInt(), any(Page.class)))
            .thenReturn(pagedRatingResponse);

        PagedRatingResponse result = ratingService.getAllRatings(requestParams);

        assertThat(result).usingRecursiveAssertion().isEqualTo(pagedRatingResponse);
        verify(ratingRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Test get all ratings but no ratings found")
    void testGetAllRatings_Empty() {
        Page<Rating> ratingPage = mock(Page.class);
        when(ratingRepository.findAll(any(Pageable.class))).thenReturn(ratingPage);
        when(ratingMapper.mapPageEntityToPagedDto(anyInt(), anyInt(), any(Page.class)))
            .thenReturn(new PagedRatingResponse(0, 10, 1, Collections.emptyList()));

        PagedRatingResponse result = ratingService.getAllRatings(requestParams);

        assertThat(result).isNotNull();
        assertThat(result.ratings()).isEmpty();
        verify(ratingRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Test getting rate by id")
    void testGetRateById() {
        Rating rating1 = rating(ratingId, fromId, toId, rideId);
        Rating rating2 = rating(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        when(ratingRepository.findTopNByToId(toId, properties.limit())).thenReturn(Arrays.asList(rating1, rating2));
        when(ratingMapper.mapToRateResponse(eq(toId), anyFloat())).thenReturn(rateResponse);

        RateResponse result = ratingService.getRateById(toId);

        assertThat(result).usingRecursiveAssertion().isEqualTo(rateResponse);
        verify(ratingRepository).findTopNByToId(toId, properties.limit());
    }

    @Test
    @DisplayName("Test getting rate by id but no ratings found")
    void testGetRateById_NoRatings() {
        when(ratingRepository.findTopNByToId(toId, properties.limit())).thenReturn(Collections.emptyList());
        when(ratingMapper.mapToRateResponse(eq(toId), anyFloat())).thenReturn(rateResponse);

        RateResponse result = ratingService.getRateById(toId);

        assertThat(result).isEqualTo(rateResponse);
        verify(ratingRepository).findTopNByToId(toId, properties.limit());
    }

    @Test
    @DisplayName("Test creating rating")
    void testCreateRating() {
        when(ratingValidator.checkRideExistenceAndPresence(any(UUID.class), any(UUID.class))).thenReturn(rideResponse);
        doNothing().when(ratingValidator).checkIfAlreadyRated(any(UUID.class), any(UUID.class));
        Rating ratingToSave = Rating.builder()
            .fromId(fromId)
            .toId(toId)
            .rideId(rideId)
            .rating(4.5f)
            .comment("Great ride")
            .build();
        when(ratingMapper.mapRequestToEntity(ratingRequest, fromId, toId)).thenReturn(ratingToSave);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingRepository.findTopNByToId(toId, 0)).thenReturn(List.of(ratingToSave));
        when(ratingMapper.mapToRateResponse(toId, 4.5f)).thenReturn(rateResponse);

        UUID result = ratingService.createRating(ratingRequest, fromId);

        assertThat(result).isEqualTo(ratingId);
        verify(ratingChangeEventRepository).save(any(RatingChangeEvent.class));
    }

    @Test
    @DisplayName("Test creating rating but ride doesn't exist")
    void testCreateRating_RideNotFound() {
        when(ratingValidator.checkRideExistenceAndPresence(any(UUID.class), any(UUID.class)))
            .thenThrow(new RideNotFoundException("Ride with id " + rideId + " not found"));

        assertThatThrownBy(() -> ratingService.createRating(ratingRequest, fromId))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage("Ride with id " + rideId + " not found");

        verify(ratingValidator).checkRideExistenceAndPresence(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Test creating rating but already rated")
    void testCreateRating_DuplicateRating() {
        RideResponse rideResponse = new RideResponse(UUID.randomUUID(), fromId, UUID.randomUUID());
        when(ratingValidator.checkRideExistenceAndPresence(any(UUID.class), any(UUID.class))).thenReturn(rideResponse);
        doThrow(new RepeatedRatingAttemptException(REPEATED_ATTEMPT_MESSAGE.formatted(fromId, "rate")))
            .when(ratingValidator).checkIfAlreadyRated(any(UUID.class), any(UUID.class));

        assertThatThrownBy(() -> ratingService.createRating(ratingRequest, fromId))
            .isInstanceOf(RepeatedRatingAttemptException.class)
            .hasMessage(REPEATED_ATTEMPT_MESSAGE.formatted(fromId, "rate"));

        verify(ratingValidator).checkIfAlreadyRated(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Test adding ride comment")
    void testAddRideComment() {
        doNothing().when(ratingValidator).checkRatingExistence(ratingId, rideComment(ratingId).fromId());
        doNothing().when(ratingValidator).checkIfAlreadyCommented(ratingId);

        Rating ratingWithoutComment = Rating.builder()
            .fromId(fromId)
            .toId(toId)
            .rideId(rideId)
            .rating(4.5f)
            .build();

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(ratingWithoutComment));
        when(ratingMapper.mapDtoToEntityCommentUpdate(commentRequest, ratingWithoutComment)).thenReturn(rating);
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingMapper.mapEntityToResponse(rating)).thenReturn(ratingResponse);

        RatingResponse result = ratingService.addRideComment(ratingId, commentRequest);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveAssertion().isEqualTo(ratingResponse);
        verify(ratingRepository).save(rating);
    }

    @Test
    @DisplayName("Test adding ride comment but rating does not exist")
    void testAddRideComment_RatingNotFound() {
        doThrow(new RatingNotFoundException(RATING_NOT_FOUND_MESSAGE.formatted(fromId))).when(ratingValidator)
            .checkRatingExistence(any(UUID.class), any(UUID.class));

        assertThatThrownBy(() -> ratingService.addRideComment(ratingId, commentRequest))
            .isInstanceOf(RatingNotFoundException.class)
            .hasMessage(RATING_NOT_FOUND_MESSAGE.formatted(fromId));

        verify(ratingValidator).checkRatingExistence(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Test adding ride comment but rating already commented")
    void testAddRideComment_DuplicateComment() {
        doNothing().when(ratingValidator).checkRatingExistence(any(UUID.class), any(UUID.class));
        doThrow(new RepeatedRatingAttemptException(REPEATED_ATTEMPT_MESSAGE.formatted(ratingId, "comment")))
            .when(ratingValidator).checkIfAlreadyCommented(any(UUID.class));

        assertThatThrownBy(() -> ratingService.addRideComment(ratingId, commentRequest))
            .isInstanceOf(RepeatedRatingAttemptException.class)
            .hasMessage(REPEATED_ATTEMPT_MESSAGE.formatted(ratingId, "comment"));

        verify(ratingValidator).checkIfAlreadyCommented(any(UUID.class));
    }

}