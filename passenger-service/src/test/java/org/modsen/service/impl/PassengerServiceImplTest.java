package org.modsen.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.modsen.util.TestUtil.pagedPassengerResponse;
import static org.modsen.util.TestUtil.passenger;
import static org.modsen.util.TestUtil.passengerDtoRequest;
import static org.modsen.util.TestUtil.passengerDtoResponse;
import static org.modsen.util.TestUtil.passengerWithRatingResponse;
import static org.modsen.util.TestUtil.rateResponse;
import static org.modsen.util.TestUtil.requestParams;
import static org.modsen.util.constant.ExceptionConstant.PASSENGER_NOT_FOUND_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_EMAIL_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedPassengerResponse;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.dto.response.PassengerWithRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.entity.Passenger;
import org.modsen.exception.PassengerNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.mapper.PassengerMapper;
import org.modsen.repository.PassengerRepository;
import org.modsen.validator.PassengerValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    private Clock clock;

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private PassengerValidator passengerValidator;

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private UUID passengerId;
    private Clock clockFix;
    private RequestParams requestParams = requestParams();
    private Passenger passenger;
    private PassengerRequest passengerRequest;
    private RateResponse rateResponse;
    private PassengerResponse passengerResponse;


    @BeforeEach
    void setUp() {
        passengerId = UUID.randomUUID();
        clock = Clock.systemDefaultZone();
        clockFix = Clock.fixed(Instant.parse("2025-02-23T10:15:30Z"), ZoneId.of("UTC"));
        requestParams = requestParams();
        passenger = passenger(passengerId, clock);
        passengerRequest = passengerDtoRequest();
        rateResponse = rateResponse(passengerId);
        passengerResponse = passengerDtoResponse(passengerId, "buil@modsen.com", clock);
    }

    @Test
    @DisplayName("Test getting all passengers")
    void testGetAllPassengers() {
        PagedPassengerResponse expectedResponse = pagedPassengerResponse(requestParams, List.of(passengerResponse));

        Page<Passenger> passengerPage = new PageImpl<>(List.of(passenger));
        when(passengerRepository.findByIsDeletedIsFalse(any(Pageable.class))).thenReturn(passengerPage);
        when(passengerMapper.mapPageEntityToPagedDto(requestParams.page(), requestParams.limit(), passengerPage))
            .thenReturn(expectedResponse);

        PagedPassengerResponse actualResponse = passengerService.getAllPassengers(requestParams);

        assertNotNull(actualResponse);
        verify(passengerRepository).findByIsDeletedIsFalse(any(Pageable.class));
        verify(passengerMapper).mapPageEntityToPagedDto(requestParams.page(), requestParams.limit(), passengerPage);
        assertEquals(expectedResponse.passengers().size(), actualResponse.passengers().size());
        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Test getting all passengers but repository returns empty")
    void testGetAllPassengers_Empty() {
        PagedPassengerResponse expectedResponse = pagedPassengerResponse(requestParams, List.of());
        when(passengerRepository.findByIsDeletedIsFalse(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        when(passengerMapper.mapPageEntityToPagedDto(requestParams.page(), requestParams.limit(),
            new PageImpl<>(List.of()))).thenReturn(expectedResponse);

        PagedPassengerResponse actualResponse = passengerService.getAllPassengers(requestParams);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.passengers().size());
    }

    @Test
    @DisplayName("Test getting passenger by id")
    void testGetPassengerById() {
        PassengerWithRatingResponse expectedResponse = passengerWithRatingResponse(passengerId, 4.5f);
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(passengerMapper.mapEntityToPassengerIdWithRating(passenger))
            .thenReturn(expectedResponse);

        PassengerWithRatingResponse actualResponse = passengerService.getPassengerById(passengerId);

        assertNotNull(actualResponse);
        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
        verify(passengerValidator).checkExistenceAndPresence(passengerId);
    }

    @Test
    @DisplayName("Test getting passenger by id but passenger not found")
    void testGetPassengerById_NotFound() {
        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE
            .formatted(passengerId))).when(passengerValidator).checkExistenceAndPresence(passengerId);

        assertThrows(PassengerNotFoundException.class, () -> passengerService.getPassengerById(passengerId));
    }

    @Test
    @DisplayName("Test registering the passenger")
    void testRegisterPassenger() {
        when(passengerMapper.mapDtoToEntity(passengerRequest)).thenReturn(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        UUID actualPassengerId = passengerService.registerPassenger(passengerRequest);

        assertEquals(passengerId, actualPassengerId);
        verify(passengerValidator).checkUniqueness(passengerRequest);
    }

    @Test
    @DisplayName("Test registering passenger but phone number already exists")
    void testRegisterPassenger_DuplicatePhone() {
        doThrow(new RepeatedDataException(REPEATED_PHONE_NUMBER_MESSAGE.formatted(passengerRequest.phoneNumber())))
            .when(passengerValidator).checkUniqueness(passengerRequest);

        assertThrows(RepeatedDataException.class, () -> passengerService.registerPassenger(passengerRequest));
    }

    @Test
    @DisplayName("Test registering passenger but email already exists")
    void testRegisterPassenger_DuplicateEmail() {
        doThrow(new RepeatedDataException(REPEATED_EMAIL_MESSAGE.formatted(passengerRequest.email())))
            .when(passengerValidator).checkUniqueness(passengerRequest);

        assertThrows(RepeatedDataException.class, () -> passengerService.registerPassenger(passengerRequest));
    }

    @Test
    @DisplayName("Test updating the passenger")
    void testUpdatePassenger() {
        LocalDateTime fixedLocalDateTime = LocalDateTime.now(clockFix);
        when(passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId)).thenReturn(passenger);
        when(passengerMapper.mapDtoToEntity(passengerRequest, passengerId,
            passenger.getCreatedAt(), LocalDateTime.now(clockFix))).thenReturn(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(passengerMapper.mapEntityToDto(passenger)).thenReturn(passengerResponse);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class))).thenReturn(fixedLocalDateTime);
            PassengerResponse actualPassengerResponse = passengerService.updatePassenger(passengerId, passengerRequest);

            assertThat(actualPassengerResponse).usingRecursiveAssertion().isEqualTo(passengerResponse);
        }

        verify(passengerValidator).checkExistenceAndPresence(passengerId);
        verify(passengerValidator).checkUniqueness(passengerId, passengerRequest);
    }

    @Test
    @DisplayName("Test updating the passenger but passenger not found")
    void testUpdatePassenger_NotFound() {
        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE
            .formatted(passengerId))).when(passengerValidator).checkExistenceAndPresence(passengerId);

        assertThrows(PassengerNotFoundException.class,
            () -> passengerService.updatePassenger(passengerId, passengerRequest));
    }

    @Test
    @DisplayName("Test updating passenger but phone number already exists")
    void testUpdatePassenger_DuplicatePhone() {
        doThrow(new RepeatedDataException(REPEATED_PHONE_NUMBER_MESSAGE.formatted(passengerRequest.phoneNumber())))
            .when(passengerValidator).checkUniqueness(passengerId, passengerRequest);

        assertThrows(RepeatedDataException.class, () -> passengerService.updatePassenger(passengerId, passengerRequest));
    }

    @Test
    @DisplayName("Test updating passenger but email already exists")
    void testUpdatePassenger_DuplicateEmail() {
        doThrow(new RepeatedDataException(REPEATED_EMAIL_MESSAGE.formatted(passengerRequest.email())))
            .when(passengerValidator).checkUniqueness(passengerId, passengerRequest);

        assertThrows(RepeatedDataException.class, () -> passengerService.updatePassenger(passengerId, passengerRequest));
    }

    @Test
    @DisplayName("Test deleting the passenger")
    void testDeletePassenger() {
        when(passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId)).thenReturn(passenger);

        passengerService.deletePassenger(passengerId);

        assertTrue(passenger.getIsDeleted());
        verify(passengerValidator).checkExistenceAndPresence(passengerId);
        verify(passengerRepository).save(passenger);
    }

    @Test
    @DisplayName("Test deleting the passenger but passenger not found")
    void testDeletePassenger_NotFound() {
        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE
            .formatted(passengerId))).when(passengerValidator).checkExistenceAndPresence(passengerId);

        assertThrows(PassengerNotFoundException.class, () -> passengerService.deletePassenger(passengerId));
    }

    @Test
    @DisplayName("Test adding photo to the passenger")
    void testAddPhoto() {
        String fileRef = "photo_reference";
        when(passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId)).thenReturn(passenger);
        passenger.setProfilePictureRef(fileRef);
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        UUID result = passengerService.addPhoto(passengerId, fileRef);

        assertEquals(passengerId, result);
        assertEquals(fileRef, passenger.getProfilePictureRef());
        verify(passengerValidator).checkExistenceAndPresence(passengerId);
    }

    @Test
    @DisplayName("Test add photo to the passenger but passenger not found")
    void testAddPhoto_NotFound() {
        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE
            .formatted(passengerId))).when(passengerValidator).checkExistenceAndPresence(passengerId);

        assertThrows(PassengerNotFoundException.class, () -> passengerService.addPhoto(passengerId, "photo_ref"));
    }

    @Test
    @DisplayName("Test updating the passenger rating")
    void testUpdatePassengerRating() {
        UUID passengerId = UUID.fromString(rateResponse.toId());
        when(passengerRepository.findByPassengerIdAndIsDeletedIsFalse(passengerId)).thenReturn(passenger);

        passengerService.updatePassengerRating(rateResponse);

        assertEquals(5, passenger.getRating());
        verify(passengerRepository).save(passenger);
        verify(passengerValidator).checkExistenceAndPresence(passengerId);
    }

    @Test
    @DisplayName("Test updating rating for the passenger but passenger not found")
    void testUpdatePassengerRating_NotFound() {
        doThrow(new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE
            .formatted(passengerId))).when(passengerValidator).checkExistenceAndPresence(passengerId);

        assertThrows(PassengerNotFoundException.class, () -> passengerService.updatePassengerRating(rateResponse));
    }

}
