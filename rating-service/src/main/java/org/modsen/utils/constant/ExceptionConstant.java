package org.modsen.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionConstant {

    public final static String RATING_NOT_FOUND_MESSAGE = "Rating with id %s not found";

    public final static String REPEATED_ATTEMPT_MESSAGE = "Participant with id %s has already left the %s for this ride";

    public final static String INVALID_RIDE_DATA_MESSAGE = "Ride with data you gave wasn't found";

}
