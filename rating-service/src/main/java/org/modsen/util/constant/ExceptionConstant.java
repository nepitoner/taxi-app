package org.modsen.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionConstant {

    public final static String RATING_NOT_FOUND_MESSAGE = "Rating with id %s not found";

    public final static String REPEATED_ATTEMPT_MESSAGE =
        "Participant with id %s has already left the %s for this ride";

    public final static String PARTICIPANT_NOT_FOUND_MESSAGE = "User with id %s you gave didn't " +
        "participate the ride with id %s";

}
