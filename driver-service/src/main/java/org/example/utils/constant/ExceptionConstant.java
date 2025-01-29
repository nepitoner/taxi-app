package org.example.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionConstant {

    public final static String DRIVER_NOT_FOUND_MESSAGE = "Driver with id %s not found";

    public final static String CAR_NOT_FOUND_MESSAGE = "Car with id %s not found";

    public final static String REPEATED_EMAIL_MESSAGE = "Driver with email %s already exists";

    public final static String REPEATED_PHONE_NUMBER_MESSAGE = "Driver with phone number %s already exists";

    public final static String EMPTY_FILE_MESSAGE = "File must not be empty";

    public final static String REPEATED_CAR_NUMBER_MESSAGE = "Car with number %s already exists";

}
