package org.modsen.validator.impl;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.modsen.util.TestUtil.carRequest;
import static org.modsen.util.constant.ExceptionConstant.CAR_NOT_FOUND_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_CAR_NUMBER_MESSAGE;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.dto.car.CarRequest;
import org.modsen.exception.CarNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.repository.CarRepository;

@ExtendWith(MockitoExtension.class)
class CarValidatorImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarValidatorImpl carValidator;

    private CarRequest carRequest;
    private UUID carId;

    @BeforeEach
    void setUp() {
        carId = UUID.randomUUID();
        carRequest = carRequest();
    }

    @Test
    @DisplayName("Test checking car number uniqueness")
    void testCheckUniqueness() {
        when(carRepository.existsByNumber(carRequest.number())).thenReturn(false);

        assertThatCode(() -> carValidator.checkUniqueness(carRequest)).doesNotThrowAnyException();

        verify(carRepository).existsByNumber(carRequest.number());
    }

    @Test
    @DisplayName("Test checking car number uniqueness but number already exists")
    void testCheckUniqueness_DuplicateNumber() {
        when(carRepository.existsByNumber(carRequest.number())).thenReturn(true);

        assertThatThrownBy(() -> carValidator.checkUniqueness(carRequest))
            .isInstanceOf(RepeatedDataException.class)
            .hasMessageContaining(REPEATED_CAR_NUMBER_MESSAGE.formatted(carRequest.number()));

        verify(carRepository).existsByNumber(carRequest.number());
    }

    @Test
    @DisplayName("Test checking car number uniqueness")
    void testCheckUniquenessWithId() {
        when(carRepository.existsByNumberAndIdIsNot(carRequest.number(), carId)).thenReturn(false);

        assertThatCode(() -> carValidator.checkUniqueness(carId, carRequest))
            .doesNotThrowAnyException();

        verify(carRepository).existsByNumberAndIdIsNot(carRequest.number(), carId);
    }

    @Test
    @DisplayName("Test checking car number with id uniqueness but the number already exists")
    void testCheckUniquenessWithId_DuplicateNumber() {
        when(carRepository.existsByNumberAndIdIsNot(carRequest.number(), carId)).thenReturn(true);

        assertThatThrownBy(() -> carValidator.checkUniqueness(carId, carRequest))
            .isInstanceOf(RepeatedDataException.class)
            .hasMessageContaining(REPEATED_CAR_NUMBER_MESSAGE.formatted(carRequest.number()));

        verify(carRepository).existsByNumberAndIdIsNot(carRequest.number(), carId);
    }

    @Test
    @DisplayName("Test checking if car not deleted")
    void testCheckExistenceAndPresenceWhenExists() {
        when(carRepository.existsByIdAndIsDeletedIsFalse(carId)).thenReturn(true);

        assertThatCode(() -> carValidator.checkExistenceAndPresence(carId))
            .doesNotThrowAnyException();

        verify(carRepository).existsByIdAndIsDeletedIsFalse(carId);
    }

    @Test
    @DisplayName("Test checking car existence but car doesn't exist")
    void testCheckExistenceAndPresenceWhenNotExists() {
        when(carRepository.existsByIdAndIsDeletedIsFalse(carId)).thenReturn(false);

        assertThatThrownBy(() -> carValidator.checkExistenceAndPresence(carId))
            .isInstanceOf(CarNotFoundException.class)
            .hasMessageContaining(CAR_NOT_FOUND_MESSAGE.formatted(carId));

        verify(carRepository).existsByIdAndIsDeletedIsFalse(carId);
    }

}