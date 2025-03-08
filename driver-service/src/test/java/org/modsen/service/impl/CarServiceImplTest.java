package org.modsen.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.modsen.util.TestUtil.car;
import static org.modsen.util.TestUtil.carRequest;
import static org.modsen.util.TestUtil.carResponse;
import static org.modsen.util.constant.ExceptionConstant.CAR_NOT_FOUND_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_CAR_NUMBER_MESSAGE;

import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.dto.car.CarRequest;
import org.modsen.dto.car.CarResponse;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedResponse;
import org.modsen.entity.Car;
import org.modsen.exception.CarNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.mapper.CarMapper;
import org.modsen.repository.CarRepository;
import org.modsen.validator.CarValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarMapper carMapper;

    @Mock
    private CarValidator carValidator;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private UUID carId;
    private Car car;
    private CarRequest carRequest;
    private CarResponse carResponse;

    @BeforeEach
    void setUp() {
        carId = UUID.randomUUID();
        car = car(carId);
        carRequest = carRequest();
        carResponse = carResponse(carId);
    }

    @Test
    @DisplayName("Test getting car by id")
    void testGetCarById() {
        doNothing().when(carValidator).checkExistenceAndPresence(carId);
        when(carRepository.findByIdAndIsDeletedIsFalse(carId)).thenReturn(car);
        when(carMapper.mapEntityToResponse(car)).thenReturn(carResponse);

        CarResponse result = carService.getCarById(carId);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveAssertion().isEqualTo(carResponse);
        verify(carValidator).checkExistenceAndPresence(carId);
        verify(carRepository).findByIdAndIsDeletedIsFalse(carId);
    }

    @Test
    @DisplayName("Test getting car by id but car doesn't exist")
    void testGetCarById_NotFound() {
        doThrow(new CarNotFoundException(CAR_NOT_FOUND_MESSAGE.formatted(carId)))
            .when(carValidator).checkExistenceAndPresence(carId);

        assertThatThrownBy(() -> carService.getCarById(carId))
            .isInstanceOf(CarNotFoundException.class)
            .hasMessage(CAR_NOT_FOUND_MESSAGE.formatted(carId));

        verify(carValidator).checkExistenceAndPresence(carId);
    }

    @Test
    @DisplayName("Test getting all cars")
    void testGetAllCars() {
        RequestParams requestParams = new RequestParams(0, 10, "createdAt", "asc");
        Page<Car> carPage = mock(Page.class);
        when(carRepository.findByIsDeletedIsFalse(any(Pageable.class))).thenReturn(carPage);
        when(carMapper.mapPageEntityToPagedDto(anyInt(), anyInt(), any(Page.class)))
            .thenReturn(new PagedResponse<>(0, 10, 1, Collections.singletonList(carResponse)));

        PagedResponse<CarResponse> result = carService.getAllCars(requestParams);

        assertThat(result).isNotNull();
        assertThat(result.elements()).hasSize(1);
        assertThat(result.totalAmount()).isEqualTo(1);
        verify(carRepository).findByIsDeletedIsFalse(any(Pageable.class));
    }

    @Test
    @DisplayName("Test creating the car")
    void testCreateCar() {
        doNothing().when(carValidator).checkUniqueness(carRequest);
        when(carMapper.mapDtoToEntity(carRequest)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);

        UUID result = carService.createCar(carRequest);

        assertThat(result).isEqualTo(carId);
        verify(carValidator).checkUniqueness(carRequest);
        verify(carMapper).mapDtoToEntity(carRequest);
        verify(carRepository).save(car);
    }

    @Test
    @DisplayName("Test creating car but number already exists")
    void testCreateCarThrows_DuplicateNumber() {
        doThrow(new RepeatedDataException(REPEATED_CAR_NUMBER_MESSAGE.formatted(carRequest.number())))
            .when(carValidator).checkUniqueness(carRequest);

        assertThatThrownBy(() -> carService.createCar(carRequest))
            .isInstanceOf(RepeatedDataException.class)
            .hasMessageContaining(REPEATED_CAR_NUMBER_MESSAGE.formatted(carRequest.number()));

        verify(carValidator).checkUniqueness(carRequest);
    }

    @Test
    @DisplayName("Test updating car")
    void testUpdateCar() {
        doNothing().when(carValidator).checkExistenceAndPresence(carId);
        doNothing().when(carValidator).checkUniqueness(carId, carRequest);
        when(carRepository.findByIdAndIsDeletedIsFalse(carId)).thenReturn(car);
        when(carMapper.mapDtoToEntity(carRequest, car)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.mapEntityToResponse(car)).thenReturn(carResponse);

        CarResponse result = carService.updateCar(carId, carRequest);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveAssertion().isEqualTo(carResponse);
        verify(carValidator).checkExistenceAndPresence(carId);
        verify(carValidator).checkUniqueness(carId, carRequest);
        verify(carRepository).findByIdAndIsDeletedIsFalse(carId);
        verify(carMapper).mapDtoToEntity(carRequest, car);
    }

    @Test
    @DisplayName("Test updating car but car doesn't exist")
    void testUpdateCar_NotFound() {
        doThrow(new CarNotFoundException(CAR_NOT_FOUND_MESSAGE.formatted(carId)))
            .when(carValidator).checkExistenceAndPresence(carId);

        assertThatThrownBy(() -> carService.updateCar(carId, carRequest))
            .isInstanceOf(CarNotFoundException.class)
            .hasMessage(CAR_NOT_FOUND_MESSAGE.formatted(carId));

        verify(carValidator).checkExistenceAndPresence(carId);
    }

    @Test
    @DisplayName("Test deleting car")
    void testDeleteCar() {
        doNothing().when(carValidator).checkExistenceAndPresence(carId);
        when(carRepository.findByIdAndIsDeletedIsFalse(carId)).thenReturn(car);

        carService.deleteCar(carId);

        assertThat(car.getIsDeleted()).isTrue();
        verify(carValidator).checkExistenceAndPresence(carId);
        verify(carRepository).findByIdAndIsDeletedIsFalse(carId);
        verify(carRepository).save(car);
    }

    @Test
    @DisplayName("Test deleting car but car doesn't exist")
    void testDeleteCarThrows_NotFound() {
        doThrow(new CarNotFoundException(CAR_NOT_FOUND_MESSAGE.formatted(carId)))
            .when(carValidator).checkExistenceAndPresence(carId);

        assertThatThrownBy(() -> carService.deleteCar(carId))
            .isInstanceOf(CarNotFoundException.class)
            .hasMessage(CAR_NOT_FOUND_MESSAGE.formatted(carId));

        verify(carValidator).checkExistenceAndPresence(carId);
    }

}