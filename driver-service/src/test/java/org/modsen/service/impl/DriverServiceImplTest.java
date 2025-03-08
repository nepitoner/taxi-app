package org.modsen.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.modsen.util.TestUtil.car;
import static org.modsen.util.TestUtil.carResponse;
import static org.modsen.util.TestUtil.driver;
import static org.modsen.util.TestUtil.driverRequest;
import static org.modsen.util.TestUtil.driverResponse;
import static org.modsen.util.constant.ExceptionConstant.DRIVER_NOT_FOUND_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
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
import org.modsen.dto.car.CarResponse;
import org.modsen.dto.driver.DriverRequest;
import org.modsen.dto.driver.DriverResponse;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.entity.Car;
import org.modsen.entity.Driver;
import org.modsen.exception.CarAlreadyTakenException;
import org.modsen.exception.DriverNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.mapper.CarMapper;
import org.modsen.mapper.DriverMapper;
import org.modsen.repository.CarRepository;
import org.modsen.repository.DriverRepository;
import org.modsen.service.CarService;
import org.modsen.validator.DriverValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private Clock clock;

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private CarMapper carMapper;

    @Mock
    private DriverValidator driverValidator;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarService carService;

    @InjectMocks
    private DriverServiceImpl driverService;

    private UUID driverId;
    private Driver driver;
    private DriverRequest driverRequest;
    private DriverResponse driverResponse;
    private UUID carId;
    private Car car;
    private CarResponse carResponse;
    private Clock clockFix;

    @BeforeEach
    void setUp() {
        driverId = UUID.randomUUID();
        clockFix = Clock.fixed(Instant.parse("2025-02-23T10:15:30Z"), ZoneId.of("UTC"));
        driver = driver(driverId, "email@gmail.com", clockFix);
        driverRequest = driverRequest();
        driverResponse = driverResponse(driverId);
        carId = UUID.randomUUID();
        car = car(carId);
        carResponse = carResponse(carId);
        clock = Clock.systemDefaultZone();
    }

    @Test
    @DisplayName("Test getting all drivers")
    void testGetAllDrivers() {
        RequestParams requestParams = new RequestParams(0, 10, "createdAt", "asc");
        Page<Driver> driverPage = mock(Page.class);
        when(driverRepository.findByIsDeletedIsFalse(any(Pageable.class))).thenReturn(driverPage);
        when(driverMapper.mapPageEntityToPagedDto(anyInt(), anyInt(), any(Page.class)))
            .thenReturn(new PagedResponse<>(0, 10, 1, Collections.singletonList(driverResponse)));

        PagedResponse<DriverResponse> result = driverService.getAllDrivers(requestParams);

        assertThat(result).isNotNull();
        assertThat(result.elements()).hasSize(1);
        assertThat(result.totalAmount()).isEqualTo(1);
        verify(driverRepository).findByIsDeletedIsFalse(any(Pageable.class));
    }

    @Test
    @DisplayName("Test registering driver")
    void testRegisterDriver() {
        doNothing().when(driverValidator).checkUniqueness(driverRequest);
        when(driverMapper.mapDtoToEntity(driverRequest)).thenReturn(driver);
        when(driverRepository.save(driver)).thenReturn(driver);

        UUID result = driverService.registerDriver(driverRequest);

        assertThat(result).isEqualTo(driverId);
        verify(driverValidator).checkUniqueness(driverRequest);
        verify(driverMapper).mapDtoToEntity(driverRequest);
        verify(driverRepository).save(driver);
    }

    @Test
    @DisplayName("Test registering driver but phone number already exists")
    void testRegisterDriver_DuplicateNumber() {
        doThrow(new RepeatedDataException(REPEATED_PHONE_NUMBER_MESSAGE.formatted(driverRequest.phoneNumber())))
            .when(driverValidator).checkUniqueness(driverRequest);

        assertThatThrownBy(() -> driverService.registerDriver(driverRequest))
            .isInstanceOf(RepeatedDataException.class)
            .hasMessage(REPEATED_PHONE_NUMBER_MESSAGE.formatted(driverRequest.phoneNumber()));

        verify(driverValidator).checkUniqueness(driverRequest);
    }

    @Test
    @DisplayName("Test updating the driver")
    void testUpdateDriver() {
        LocalDateTime fixedLocalDateTime = LocalDateTime.now(clockFix);
        Driver updatedDriver = driver(driverId, "newEmail@gmail.com", clockFix);
        doNothing().when(driverValidator).checkExistenceAndPresence(driverId);
        doNothing().when(driverValidator).checkUniqueness(driverId, driverRequest);
        when(driverRepository.findByIdAndIsDeletedIsFalse(driverId)).thenReturn(driver);
        when(driverMapper.mapDtoToEntity(driverRequest, LocalDateTime.now(clockFix), driver)).thenReturn(updatedDriver);
        when(driverRepository.save(updatedDriver)).thenReturn(updatedDriver);
        when(driverMapper.mapEntityToResponse(updatedDriver)).thenReturn(driverResponse);

        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class)))
                .thenReturn(fixedLocalDateTime);
            DriverResponse result = driverService.updateDriver(driverId, driverRequest);

            assertThat(result).usingRecursiveAssertion().isEqualTo(driverResponse);
        }
        verify(driverValidator).checkExistenceAndPresence(driverId);
        verify(driverValidator).checkUniqueness(driverId, driverRequest);
        verify(driverRepository).findByIdAndIsDeletedIsFalse(driverId);
        verify(driverMapper).mapDtoToEntity(driverRequest, LocalDateTime.now(clockFix), driver);
    }

    @Test
    @DisplayName("Test updating driver but driver doesn't exist")
    void testUpdateDriver_NotFound() {
        doThrow(new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE.formatted(driverId))).when(driverValidator)
            .checkExistenceAndPresence(driverId);

        assertThatThrownBy(() -> driverService.updateDriver(driverId, driverRequest))
            .isInstanceOf(DriverNotFoundException.class)
            .hasMessage(DRIVER_NOT_FOUND_MESSAGE.formatted(driverId));

        verify(driverValidator).checkExistenceAndPresence(driverId);
    }

    @Test
    @DisplayName("Test deleting driver")
    void testDeleteDriver() {
        doNothing().when(driverValidator).checkExistenceAndPresence(driverId);
        when(driverRepository.findByIdAndIsDeletedIsFalse(driverId)).thenReturn(driver);

        driverService.deleteDriver(driverId);

        assertThat(driver.getIsDeleted()).isTrue();
        verify(driverValidator).checkExistenceAndPresence(driverId);
        verify(driverRepository).findByIdAndIsDeletedIsFalse(driverId);
        verify(driverRepository).save(driver);
    }

    @Test
    @DisplayName("Test deleting driver but driver doesn't exist")
    void testDeleteDriver_NotFound() {
        doThrow(new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE.formatted(driverId))).when(driverValidator)
            .checkExistenceAndPresence(driverId);

        assertThatThrownBy(() -> driverService.deleteDriver(driverId))
            .isInstanceOf(DriverNotFoundException.class)
            .hasMessage(DRIVER_NOT_FOUND_MESSAGE.formatted(driverId));

        verify(driverValidator).checkExistenceAndPresence(driverId);
    }

    @Test
    @DisplayName("Test adding photo to the driver")
    void testAddPhoto() {
        String fileRef = "photo_reference";
        when(driverRepository.findByIdAndIsDeletedIsFalse(driverId)).thenReturn(driver);
        driver.setProfilePictureRef(fileRef);
        when(driverRepository.save(driver)).thenReturn(driver);

        UUID result = driverService.addPhoto(driverId, fileRef);

        assertThat(result).isEqualTo(driverId);
        assertEquals(fileRef, driver.getProfilePictureRef());
        verify(driverValidator).checkExistenceAndPresence(driverId);
    }

    @Test
    @DisplayName("Test add photo to the driver but driver not found")
    void testAddPhoto_NotFound() {
        doThrow(new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE
            .formatted(driverId))).when(driverValidator).checkExistenceAndPresence(driverId);

        assertThrows(DriverNotFoundException.class, () -> driverService.addPhoto(driverId, "photo_ref"));
    }

    @Test
    @DisplayName("Test adding a car to the driver")
    void testAddCar() {
        doNothing().when(driverValidator).checkExistenceAndPresence(driverId);
        when(driverRepository.findByIdAndIsDeletedIsFalse(driverId)).thenReturn(driver);
        when(carService.getCarById(carId)).thenReturn(carResponse);
        when(carMapper.mapResponseToEntity(any(CarResponse.class))).thenReturn(car);
        when(driverRepository.save(driver)).thenReturn(driver);
        when(carRepository.save(car)).thenReturn(car);
        when(driverMapper.mapEntityToResponse(driver)).thenReturn(driverResponse);

        DriverResponse result = driverService.addCar(driverId, carId);

        assertThat(result).isEqualTo(driverResponse);
        assertThat(driver.getCars()).contains(car);
        assertThat(car.getDrivers()).contains(driver);
        verify(driverRepository).save(driver);
        verify(carRepository).save(car);
    }

    @Test
    @DisplayName("Test adding the car to the driver but driver doesn't exist")
    void testAddCar_DriverNotFound() {
        doThrow(new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE.formatted(driverId))).when(driverValidator)
            .checkExistenceAndPresence(driverId);

        assertThatThrownBy(() -> driverService.addCar(driverId, carId))
            .isInstanceOf(DriverNotFoundException.class)
            .hasMessage(DRIVER_NOT_FOUND_MESSAGE.formatted(driverId));

        verify(driverValidator).checkExistenceAndPresence(driverId);
    }

    @Test
    @DisplayName("Test adding the car to the driver but car already taken")
    void testAddCar_CarAlreadyTaken() {
        doNothing().when(driverValidator).checkExistenceAndPresence(driverId);
        when(driverRepository.findByIdAndIsDeletedIsFalse(driverId)).thenReturn(driver);
        when(carService.getCarById(carId)).thenReturn(carResponse);
        when(carMapper.mapResponseToEntity(any(CarResponse.class))).thenReturn(car);
        car.getDrivers().add(driver(UUID.randomUUID(), "anotherem@gmail.com", clockFix));

        assertThatThrownBy(() -> driverService.addCar(driverId, carId))
            .isInstanceOf(CarAlreadyTakenException.class)
            .hasMessageContaining(carId.toString());

        verify(driverRepository, never()).save(driver);
        verify(carRepository, never()).save(car);
    }

    @Test
    @DisplayName("Test adding a car to a driver but driver doesn't exist")
    void testAddCarDriverDoesNotExist() {
        doThrow(new DriverNotFoundException("Driver not found")).when(driverValidator)
            .checkExistenceAndPresence(driverId);

        assertThatThrownBy(() -> driverService.addCar(driverId, carId))
            .isInstanceOf(DriverNotFoundException.class)
            .hasMessageContaining("Driver not found");

        verify(driverRepository, never()).findByIdAndIsDeletedIsFalse(driverId);
    }

    @Test
    @DisplayName("Test updating driver rating")
    void testUpdateDriverRating() {
        RateResponse rateResponse = new RateResponse("1", driverId.toString(), 4.5f);

        doNothing().when(driverValidator).checkExistenceAndPresence(driverId);
        when(driverRepository.findByIdAndIsDeletedIsFalse(driverId)).thenReturn(driver);

        driverService.updateDriverRating(rateResponse);

        assertThat(driver.getRating()).isEqualTo(rateResponse.rating());
        verify(driverValidator).checkExistenceAndPresence(driverId);
        verify(driverRepository).findByIdAndIsDeletedIsFalse(driverId);
        verify(driverRepository).save(driver);
    }

    @Test
    @DisplayName("Test changing driver available status")
    void testChangeDriverAvailableStatus() {
        doNothing().when(driverValidator).checkExistenceAndPresence(driverId);
        when(driverRepository.findByIdAndIsDeletedIsFalse(driverId)).thenReturn(driver);

        driverService.changeDriverAvailableStatus(driverId);

        assertThat(driver.getIsAvailable()).isFalse();
        verify(driverValidator).checkExistenceAndPresence(driverId);
        verify(driverRepository).findByIdAndIsDeletedIsFalse(driverId);
        verify(driverRepository).save(driver);
    }

}