package org.modsen.util.validator;

import org.modsen.dto.request.PassengerRequest;

import java.util.UUID;

public interface PassengerValidator {

    void checkUniqueness(PassengerRequest dto);

    void checkUniqueness(UUID passengerId, PassengerRequest dto);

    void checkExistenceAndPresence(UUID passengerId);

}
