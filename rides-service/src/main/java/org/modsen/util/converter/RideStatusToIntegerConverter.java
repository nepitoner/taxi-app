package org.modsen.util.converter;

import org.modsen.entity.RideStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class RideStatusToIntegerConverter implements Converter<RideStatus, Integer> {

    @Override
    public Integer convert(RideStatus status) {
        return status.getCode();
    }

}
