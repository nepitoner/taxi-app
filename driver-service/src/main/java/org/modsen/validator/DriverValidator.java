package org.modsen.validator;

import org.modsen.dto.driver.DriverRequest;

import java.util.UUID;

public interface DriverValidator {

    void checkUniqueness(DriverRequest dto);

    void checkUniqueness(UUID driverId, DriverRequest dto);

    void checkExistenceAndPresence(UUID driverId);

}
