package org.modsen.dto.response;

import static org.modsen.utils.constant.ExceptionConstant.INVALID_LOCATION_POINT_MESSAGE;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.modsen.exception.DistanceCalculationException;

@Setter
@Getter
public class DistanceResponse {

    private List<Route> routes;

    public double getTotalDistance() {
        if (routes != null && !routes.isEmpty()) {
            return routes.getFirst()
                .getSummary()
                .getDistance();
        }
        throw new DistanceCalculationException(INVALID_LOCATION_POINT_MESSAGE);
    }

    @Setter
    @Getter
    public static class Route {
        private Summary summary;
    }

    @Setter
    @Getter
    public static class Summary {
        private double distance;
        private double duration;
        private double ascent;
        private double descent;
    }

}
