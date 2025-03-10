package org.modsen.util.calculator;

import static org.modsen.util.constant.ExceptionConstant.INVALID_LOCATION_POINT_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_COORDINATES_MESSAGE;
import static org.modsen.util.constant.RideConstant.RIDE_PRICE_PER_KM;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modsen.exception.DistanceCalculationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RidePriceCalculator {

    private final DistanceCalculator distanceCalculator;

    public BigDecimal calculateRidePrice(List<Double> startingCoordinates, List<Double> endingCoordinates) {
        validateCoordinates(startingCoordinates, endingCoordinates);

        Point from = createPoint(startingCoordinates);
        Point to = createPoint(endingCoordinates);
        double distance = distanceCalculator.getRoadDistance(from, to);

        return RIDE_PRICE_PER_KM.multiply(BigDecimal.valueOf(distance));
    }

    private void validateCoordinates(List<Double> startingCoordinates, List<Double> endingCoordinates) {
        if (startingCoordinates.size() < 2 || endingCoordinates.size() < 2) {
            throw new DistanceCalculationException(INVALID_LOCATION_POINT_MESSAGE);
        }

        if (startingCoordinates.get(0).equals(endingCoordinates.get(0)) &&
            startingCoordinates.get(1).equals(endingCoordinates.get(1))) {
            throw new DistanceCalculationException(REPEATED_COORDINATES_MESSAGE);
        }
    }

    private Point createPoint(List<Double> coordinates) {
        return Point.builder()
            .longitude(coordinates.get(0))
            .latitude(coordinates.get(1))
            .build();
    }

}
