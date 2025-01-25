package org.example.validator;

import org.example.dto.PassengerRequest;

import java.util.UUID;

public interface PassengerValidator {

    void checkUniqueness(PassengerRequest dto);

    void checkUniqueness(UUID passengerId, PassengerRequest dto);

    void checkExistenceAndPresence(UUID passengerId);

}
