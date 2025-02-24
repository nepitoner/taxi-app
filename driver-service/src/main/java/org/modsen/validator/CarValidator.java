package org.modsen.validator;

import java.util.UUID;
import org.modsen.dto.car.CarRequest;

public interface CarValidator {

    void checkUniqueness(CarRequest dto);

    void checkUniqueness(UUID carId, CarRequest dto);

    void checkExistenceAndPresence(UUID carId);

}
