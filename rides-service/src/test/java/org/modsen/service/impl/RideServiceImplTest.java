package org.modsen.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.modsen.entity.RideStatus.CANCELED;
import static org.modsen.entity.RideStatus.CREATED;
import static org.modsen.util.TestUtil.pagedRideResponse;
import static org.modsen.util.TestUtil.requestParams;
import static org.modsen.util.TestUtil.ride;
import static org.modsen.util.TestUtil.rideRequest;
import static org.modsen.util.TestUtil.rideResponse;
import static org.modsen.util.TestUtil.shortRideResponse;
import static org.modsen.util.constant.ExceptionConstant.INVALID_RIDE_STATUS_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.RIDE_NOT_FOUND_MESSAGE;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
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
import org.modsen.client.DriverClient;
import org.modsen.client.PassengerClient;
import org.modsen.dto.request.RideAvailableEvent;
import org.modsen.dto.request.RideRequest;
import org.modsen.dto.request.RideRequestParams;
import org.modsen.dto.request.RideStatusRequest;
import org.modsen.dto.response.PagedRideResponse;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.dto.response.ShortRideResponse;
import org.modsen.entity.Ride;
import org.modsen.entity.RideStatus;
import org.modsen.exception.RideNotFoundException;
import org.modsen.exception.RideStatusProcessingException;
import org.modsen.mapper.RideMapper;
import org.modsen.repository.RideRepository;
import org.modsen.service.KafkaMessagingService;
import org.modsen.util.calculator.RidePriceCalculator;
import org.modsen.validator.RideValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private Clock clock;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideValidator rideValidator;

    @Mock
    private PassengerClient passengerClient;

    @Mock
    private DriverClient driverClient;

    @Mock
    private RidePriceCalculator ridePriceCalculator;

    @Mock
    private KafkaMessagingService kafkaMessagingService;

    @InjectMocks
    private RideServiceImpl rideService;

    private UUID rideId;
    private UUID driverId;
    private UUID passengerId;
    private RideRequestParams requestParams;
    private RideRequest rideRequest;
    private RideResponse rideResponse;
    private ShortRideResponse shortRideResponse;
    private PagedRideResponse pagedRideResponse;
    private Ride ride;
    private RideStatusRequest rideStatusRequest;
    private Clock clockFix;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        rideId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        passengerId = UUID.randomUUID();
        requestParams = requestParams();
        rideRequest = rideRequest(passengerId);
        ride = ride(rideId, driverId, passengerId, CREATED);
        rideResponse = rideResponse(rideId, driverId, passengerId, CREATED);
        pagedRideResponse = pagedRideResponse(rideId, driverId, passengerId);
        shortRideResponse = shortRideResponse(rideId, driverId, passengerId);
        rideStatusRequest = new RideStatusRequest("ACCEPTED");
        clockFix = Clock.fixed(Instant.parse("2025-02-23T10:15:30Z"), ZoneId.of("UTC"));
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        pageable = PageRequest.of(requestParams.page(), requestParams.limit(), sort);
    }

    @Test
    @DisplayName("Test getting all rides")
    void testGetAllRides() {
        Page<Ride> ridePage = mock(Page.class);
        when(rideRepository.findAll(any(Pageable.class))).thenReturn(ridePage);
        when(rideMapper.mapPageEntityToPagedDto(anyInt(), anyInt(), any(Page.class)))
            .thenReturn(pagedRideResponse);

        PagedRideResponse result = rideService.getAllRides(requestParams);

        assertThat(result).usingRecursiveAssertion().isEqualTo(pagedRideResponse);
        verify(rideRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Test getting all rides but no rides found")
    void testGetAllRides_Empty() {
        Page<Ride> ridePage = mock(Page.class);
        when(rideRepository.findAll(any(Pageable.class))).thenReturn(ridePage);
        when(rideMapper.mapPageEntityToPagedDto(anyInt(), anyInt(), any(Page.class)))
            .thenReturn(new PagedRideResponse(0, 10, 1, Collections.emptyList()));

        PagedRideResponse result = rideService.getAllRides(requestParams);

        assertThat(result).isNotNull();
        assertThat(result.rides()).isEmpty();
        verify(rideRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Test getting all ride by driver id")
    void testGetAllRidesByDriverId() {
        List<Ride> rides = List.of(ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findByDriverId(driverId, pageable)).thenReturn(ridePage);
        when(rideMapper.mapPageEntityToPagedDto(anyInt(), anyInt(), any(Page.class)))
            .thenReturn(pagedRideResponse);

        PagedRideResponse response = rideService.getAllRidesByDriverId(requestParams, driverId);

        assertThat(response).isNotNull();
        verify(rideRepository).findByDriverId(driverId, pageable);
    }

    @Test
    @DisplayName("Test getting all rides by driver id by limit more than 50")
    void testGetAllRidesByDriverId_ExceedLimit() {
        requestParams = new RideRequestParams(0, 100, "createdAt", "asc");
        List<Ride> rides = List.of(ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        Pageable pageable = PageRequest.of(requestParams.page(), 50, sort);
        when(rideRepository.findByDriverId(driverId, pageable)).thenReturn(ridePage);
        when(rideMapper.mapPageEntityToPagedDto(0, 50, ridePage)).thenReturn(pagedRideResponse);

        PagedRideResponse response = rideService.getAllRidesByDriverId(requestParams, driverId);

        assertThat(response).isNotNull();
        verify(rideRepository).findByDriverId(driverId, pageable);
    }

    @Test
    @DisplayName("Test getting all ride by driver id but driver has no rides")
    void testGetAllRidesByDriverId_NoRides() {
        List<Ride> rides = Collections.emptyList();
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findByDriverId(driverId, pageable)).thenReturn(ridePage);
        when(rideMapper.mapPageEntityToPagedDto(0, 10, ridePage)).thenReturn(pagedRideResponse);

        PagedRideResponse response = rideService.getAllRidesByDriverId(requestParams, driverId);

        assertThat(response).isNotNull();
        verify(rideRepository).findByDriverId(driverId, pageable);
    }

    @Test
    @DisplayName("Test getting all rides by passenger id")
    void testGetAllRidesByPassengerId() {
        List<Ride> rides = List.of(ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findByPassengerId(passengerId, pageable)).thenReturn(ridePage);
        when(rideMapper.mapPageEntityToPagedDto(0, 10, ridePage)).thenReturn(pagedRideResponse);

        PagedRideResponse response = rideService.getAllRidesByPassengerId(requestParams, passengerId);

        assertThat(response).isNotNull();
        verify(rideRepository).findByPassengerId(passengerId, pageable);
        verify(rideMapper).mapPageEntityToPagedDto(0, 10, ridePage);
    }

    @Test
    @DisplayName("Test getting all rides by passenger id but passenger has no rides")
    void testGetAllRidesByPassengerId_NoRides() {
        List<Ride> rides = Collections.emptyList();
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findByPassengerId(passengerId, pageable)).thenReturn(ridePage);
        when(rideMapper.mapPageEntityToPagedDto(requestParams.page(), requestParams.limit(), ridePage)).thenReturn(pagedRideResponse);

        PagedRideResponse response = rideService.getAllRidesByPassengerId(requestParams, passengerId);

        assertThat(response).isNotNull();
        verify(rideRepository).findByPassengerId(passengerId, pageable);
    }

    @Test
    @DisplayName("Test getting ride by id")
    void testGetRideById() {
        doNothing().when(rideValidator).checkExistence(rideId);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideMapper.mapEntityToShortResponse(ride)).thenReturn(shortRideResponse);

        ShortRideResponse result = rideService.getRideById(rideId);

        assertThat(result).usingRecursiveAssertion().isEqualTo(shortRideResponse);
        verify(rideValidator).checkExistence(rideId);
    }

    @Test
    @DisplayName("Test getting ride by id but ride doesn't exist")
    void testGetRideById_NotFound() {
        doThrow(new RideNotFoundException(RIDE_NOT_FOUND_MESSAGE.formatted(rideId))).when(rideValidator)
            .checkExistence(rideId);

        assertThatThrownBy(() -> rideService.getRideById(rideId))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage(RIDE_NOT_FOUND_MESSAGE.formatted(rideId));

        verify(rideValidator).checkExistence(rideId);
    }

    @Test
    @DisplayName("Test creating ride")
    void testCreateRide() {
        BigDecimal price = new BigDecimal("10.00");
        LocalDateTime fixedLocalDateTime = LocalDateTime.now(clockFix);
        when(passengerClient.getPassengerById(passengerId)).thenReturn(new PassengerResponse(passengerId, 4.5f));
        when(ridePriceCalculator.calculateRidePrice(any(), any())).thenReturn(price);
        when(rideMapper.mapRequestToEntity(rideRequest, LocalDateTime.now(clockFix), price)).thenReturn(ride);
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class)))
                    .thenReturn(fixedLocalDateTime);
            UUID result = rideService.createRide(rideRequest);

            assertThat(result).isEqualTo(rideId);
        }

        verify(kafkaMessagingService).sendMessage(any(RideAvailableEvent.class));
    }

    @Test
    @DisplayName("Test updating ride")
    void testUpdateRide() {
        doNothing().when(rideValidator).checkExistence(rideId);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideMapper.mapDtoToEntity(any(RideRequest.class), any(Ride.class))).thenReturn(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.mapEntityToResponse(ride)).thenReturn(rideResponse);

        RideResponse result = rideService.updateRide(rideId, rideRequest);

        assertThat(result).usingRecursiveAssertion().isEqualTo(rideResponse);
        verify(rideValidator).checkExistence(rideId);
    }

    @Test
    @DisplayName("Test updating ride but ride doesn't exist")
    void testUpdateRide_NotFound() {
        doThrow(new RideNotFoundException(RIDE_NOT_FOUND_MESSAGE.formatted(rideId))).when(rideValidator)
            .checkExistence(rideId);

        assertThatThrownBy(() -> rideService.updateRide(rideId, rideRequest))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage(RIDE_NOT_FOUND_MESSAGE.formatted(rideId));

        verify(rideValidator).checkExistence(rideId);
    }

    @Test
    @DisplayName("Test changing ride status")
    void testChangeRideStatus() {
        doNothing().when(rideValidator).checkExistence(rideId);
        doNothing().when(rideValidator).checkStatusProcessing(rideStatusRequest, rideId);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideMapper.mapDtoToEntityStatusUpdate(rideStatusRequest, ride)).thenReturn(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.mapEntityToResponse(ride)).thenReturn(rideResponse);

        RideResponse result = rideService.changeRideStatus(rideId, rideStatusRequest);

        assertThat(result).usingRecursiveAssertion().isEqualTo(rideResponse);
        verify(rideValidator).checkExistence(rideId);
        verify(rideValidator).checkStatusProcessing(rideStatusRequest, rideId);
        verify(rideRepository).save(ride);
    }

    @Test
    @DisplayName("Test changing ride status but ride doesn't exist")
    void testChangeRideStatus_NotFound() {
        doThrow(new RideNotFoundException(RIDE_NOT_FOUND_MESSAGE.formatted(rideId))).when(rideValidator)
            .checkExistence(rideId);

        assertThatThrownBy(() -> rideService.changeRideStatus(rideId, rideStatusRequest))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage(RIDE_NOT_FOUND_MESSAGE.formatted(rideId));

        verify(rideValidator).checkExistence(rideId);
        verify(rideRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Test changing ride status but status processing check fails")
    void testChangeRideStatus_StatusProcessingFailed() {
        String newStatus = "COMPLETED";
        doNothing().when(rideValidator).checkExistence(rideId);
        doThrow(new RideStatusProcessingException(INVALID_RIDE_STATUS_MESSAGE.formatted(newStatus))).when(rideValidator)
            .checkStatusProcessing(rideStatusRequest, rideId);

        assertThatThrownBy(() -> rideService.changeRideStatus(rideId, rideStatusRequest))
            .isInstanceOf(RideStatusProcessingException.class)
            .hasMessage(INVALID_RIDE_STATUS_MESSAGE.formatted(newStatus));

        verify(rideValidator).checkExistence(rideId);
        verify(rideValidator).checkStatusProcessing(rideStatusRequest, rideId);
        verify(rideRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test changing ride status to CANCELED without processing check")
    void testChangeRideStatus_CancelledStatus() {
        rideStatusRequest = new RideStatusRequest("CANCELED");
        Ride canceledRide = ride(rideId, driverId, passengerId, CANCELED);
        RideResponse canceledResponse = rideResponse(rideId, driverId, passengerId, CANCELED);
        doNothing().when(rideValidator).checkExistence(rideId);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideMapper.mapDtoToEntityStatusUpdate(rideStatusRequest, ride))
                .thenReturn(canceledRide);
        when(rideRepository.save(canceledRide)).thenReturn(canceledRide);
        when(rideMapper.mapEntityToResponse(canceledRide)).thenReturn(canceledResponse);

        RideResponse result = rideService.changeRideStatus(rideId, rideStatusRequest);

        assertThat(result).usingRecursiveAssertion().isEqualTo(canceledResponse);
        verify(rideValidator).checkExistence(rideId);
        verify(rideValidator, never()).checkStatusProcessing(any(), any());
    }

    @Test
    @DisplayName("Test accepting ride")
    void testAcceptRide() {
        doNothing().when(rideValidator).checkExistence(rideId);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideMapper.mapDtoToEntityStatusUpdate(rideStatusRequest, ride)).thenReturn(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        doNothing().when(driverClient).changeDriverAvailableStatus(driverId);

        RideResponse rideResponse = RideResponse.builder()
            .rideId(rideId)
            .driverId(driverId)
            .passengerId(ride.getPassengerId())
            .startingCoordinates(ride.getStartingCoordinates())
            .endingCoordinates(ride.getEndingCoordinates())
            .rideStatus(RideStatus.ACCEPTED)
            .orderDateTime(ride.getOrderDateTime())
            .price(ride.getPrice())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(rideService.changeRideStatus(rideId, RideStatusRequest.builder().rideStatus("ACCEPTED").build()))
            .thenReturn(rideResponse);

        RideResponse result = rideService.acceptRide(rideId, driverId);

        assertThat(result).usingRecursiveAssertion().isEqualTo(rideResponse);
        verify(rideValidator, times(3)).checkExistence(rideId);
        verify(rideRepository, times(3)).findById(rideId);
        verify(rideRepository, times(3)).save(ride);
        verify(driverClient).changeDriverAvailableStatus(driverId);
    }

    @Test
    @DisplayName("Test accepting ride but ride does not exist")
    void testAcceptRide_NotFound() {
        doThrow(new RideNotFoundException("Ride not found")).when(rideValidator).checkExistence(rideId);

        assertThatThrownBy(() -> rideService.acceptRide(rideId, driverId))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage("Ride not found");

        verify(rideValidator).checkExistence(rideId);
    }

    @Test
    @DisplayName("Test declining ride")
    void testDeclineRide() {
        rideStatusRequest = new RideStatusRequest("CANCELED");
        Ride canceledRide = ride(rideId, driverId, passengerId, CANCELED);
        RideResponse canceledResponse = rideResponse(rideId, driverId, passengerId, CANCELED);
        doNothing().when(rideValidator).checkExistence(rideId);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideMapper.mapDtoToEntityStatusUpdate(rideStatusRequest, ride))
                .thenReturn(canceledRide);
        when(rideRepository.save(canceledRide)).thenReturn(canceledRide);
        when(rideMapper.mapEntityToResponse(canceledRide)).thenReturn(canceledResponse);

        RideResponse result = rideService.declineRide(rideId, driverId);

        assertThat(result).usingRecursiveAssertion().isEqualTo(canceledResponse);
        verify(rideValidator).checkExistence(rideId);
    }

    @Test
    @DisplayName("Test declining ride but ride does not exist")
    void testDeclineRide_NotFound() {
        doThrow(new RideNotFoundException(RIDE_NOT_FOUND_MESSAGE.formatted(rideId))).when(rideValidator)
            .checkExistence(rideId);

        assertThatThrownBy(() -> rideService.declineRide(rideId, driverId))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage(RIDE_NOT_FOUND_MESSAGE.formatted(rideId));

        verify(rideValidator).checkExistence(rideId);
    }

}