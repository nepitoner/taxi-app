package org.modsen.validator;

import org.modsen.dto.car.CarRequest;

import java.util.UUID;

public interface CarValidator {

    void checkUniqueness(CarRequest dto);

    void checkUniqueness(UUID carId, CarRequest dto);

    void checkExistenceAndPresence(UUID carId);

}
