package org.example.utils.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConstant {

    public final static String NOT_FOUND_MESSAGE = "Passenger with id %s not found";

    public final static String REPEATED_DATA_MESSAGE = "Passenger with phone %s or email %s already exists";

    public final static String EMPTY_FILE_MESSAGE = "File must not be empty";

}
