package org.example.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DriverServiceConstant {

    public final static String PHONE_NUMBER_REGEX = "^\\+375(29|25|33|44)\\d{7}$";

    public final static String CAR_NUMBER_REGEX = "^\\d{4}[A-Za-z]{2}-[1-7]$";

}
