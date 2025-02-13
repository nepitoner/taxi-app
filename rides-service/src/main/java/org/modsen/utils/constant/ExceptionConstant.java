package org.modsen.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionConstant {

    public final static String RIDE_NOT_FOUND_MESSAGE = "Ride with id %s not found";

    public final static String UNKNOWN_CODE_MESSAGE = "Unknown status code %d";

    public final static String INVALID_ENUM_MESSAGE = "Invalid enum value";

    public final static String INVALID_RIDE_STATUS_MESSAGE = "Ride status %s is not allowed here";

    public final static String INVALID_RIDE_STATUS_CHANGE_MESSAGE = "You can't change status from %s";

    public final static String INVALID_LOCATION_POINT_MESSAGE =
            "Coordinates must contain at least two values (longitude and latitude)";

    public final static String REPEATED_COORDINATES_MESSAGE = "Starting and ending coordinates must not be the same";

    public final static String SERVICE_IS_NOT_AVAILABLE_MESSAGE = "Service isn't available";

    public final static String PARTICIPANT_NOT_FOUND_MESSAGE = "Neither passenger and driver with id %s " +
            "participated in ride with id %s";

}
