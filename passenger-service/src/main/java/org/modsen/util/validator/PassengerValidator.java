package org.modsen.util.validator;

import java.util.UUID;
import org.modsen.dto.request.PassengerRequest;

public interface PassengerValidator {

    void checkUniqueness(PassengerRequest dto);

    void checkUniqueness(UUID passengerId, PassengerRequest dto);

    void checkExistenceAndPresence(UUID passengerId);

}
