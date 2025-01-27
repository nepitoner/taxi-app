package org.example.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public final class ExceptionConstant {

    public final static String PASSENGER_NOT_FOUND_MESSAGE = "Passenger with id %s not found";

    public final static String REPEATED_EMAIL_MESSAGE = "Passenger with email %s already exists";

    public final static String REPEATED_PHONE_NUMBER_MESSAGE = "Passenger with phone number %s already exists";

    public final static String EMPTY_FILE_MESSAGE = "File must not be empty";

}
