package org.modsen.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DriverServiceConstant {

    public final static String PHONE_NUMBER_REGEX = "^\\+375(29|25|33|44)\\d{7}$";

    public final static String CAR_NUMBER_REGEX = "^\\d{4}[A-Za-z]{2}-[1-7]$";

    public final static String DRIVER_PHOTO_NAME = "driver_photo_%s";

    public final static String EMAIL_SUBJECT = "Available ride for you";

    public final static String EMAIL_BUTTONS = "<h1>Available ride</h1>" +
        "<p>Passenger with rating %f </p>" +
        "<a href='http://mysite/accept?rideId=%s&driverId=%s' " +
        "style='padding:10px; background-color:green; " +
        "color:white; text-decoration:none;'>Yes</a> " +
        "<a href='http://mysite/decline?rideId=%s&driverId=%s" +
        " style='padding:10px; background-color:red; color:white; text-decoration:none;'>No</a>";

}
