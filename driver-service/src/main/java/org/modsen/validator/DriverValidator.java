package org.modsen.validator;

import java.util.UUID;
import org.modsen.dto.driver.DriverRequest;

public interface DriverValidator {

    void checkUniqueness(DriverRequest dto);

    void checkUniqueness(UUID driverId, DriverRequest dto);

    void checkExistenceAndPresence(UUID driverId);

}
