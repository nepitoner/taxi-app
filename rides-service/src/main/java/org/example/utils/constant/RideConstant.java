package org.example.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RideConstant {

    public final static BigDecimal RIDE_PRICE_PER_KM = BigDecimal.TEN;
    
    public final static String COORDINATES_FORMAT = "{\"coordinates\":[[%f,%f],[%f,%f]]}";

}
