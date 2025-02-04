package org.example.validator.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.car.CarRequest;
import org.example.exception.CarNotFoundException;
import org.example.exception.RepeatedDataException;
import org.example.repository.CarRepository;
import org.example.utils.constant.ExceptionConstant;
import org.example.validator.CarValidator;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
