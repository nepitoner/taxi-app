package org.modsen.util.constant;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RideConstant {

    public final static BigDecimal RIDE_PRICE_PER_KM = BigDecimal.TEN;

    public final static String COORDINATES_FORMAT = "{\"coordinates\":[[%f,%f],[%f,%f]]}";

}
