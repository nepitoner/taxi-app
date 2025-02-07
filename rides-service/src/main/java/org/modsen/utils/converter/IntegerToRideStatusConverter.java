package org.modsen.utils.converter;

import org.modsen.entity.RideStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IntegerToRideStatusConverter implements Converter<Integer, RideStatus> {

    @Override
    public RideStatus convert(Integer source) {
        return RideStatus.fromCode(source);
    }

}
