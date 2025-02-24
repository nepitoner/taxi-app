package org.modsen.validator.impl;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modsen.dto.car.CarRequest;
import org.modsen.exception.CarNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.repository.CarRepository;
import org.modsen.utils.constant.ExceptionConstant;
import org.modsen.validator.CarValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarValidatorImpl implements CarValidator {

    private final CarRepository carRepository;

    @Override
    public void checkUniqueness(CarRequest dto) {
        if (carRepository.existsByNumber(dto.number())) {
            throw new RepeatedDataException(
                ExceptionConstant.REPEATED_CAR_NUMBER_MESSAGE.formatted(dto.number()));
        }
    }

    @Override
    public void checkUniqueness(UUID carId, CarRequest dto) {
        if (carRepository.existsByNumberAndIdIsNot(dto.number(), carId)) {
            throw new RepeatedDataException(
                ExceptionConstant.REPEATED_CAR_NUMBER_MESSAGE.formatted(dto.number()));
        }

    }

    @Override
    public void checkExistenceAndPresence(UUID carId) {
        if (!carRepository.existsByIdAndIsDeletedIsFalse(carId)) {
            throw new CarNotFoundException(
                ExceptionConstant.CAR_NOT_FOUND_MESSAGE.formatted(carId));
        }
    }

}
