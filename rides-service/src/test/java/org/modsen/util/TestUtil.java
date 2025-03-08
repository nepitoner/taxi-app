package org.modsen.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.modsen.dto.request.RideRequest;
import org.modsen.dto.request.RideRequestParams;
import org.modsen.dto.response.PagedRideResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.dto.response.ShortRideResponse;
import org.modsen.entity.Ride;
import org.modsen.entity.RideStatus;

public final class TestUtil {

    private TestUtil() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .registerModule(new JavaTimeModule());

    public static String asJson(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static RideRequestParams requestParams() {
        return RideRequestParams.builder()
            .page(0)
            .limit(10)
            .sortBy("createdAt")
            .sortDirection("asc")
            .build();
    }

    public static RideRequest rideRequest(UUID passengerId) {
        return new RideRequest(
            passengerId,
            Arrays.asList(40.7128, -74.0060),
            Arrays.asList(34.0522, -118.2437)
        );
    }

    public static Ride ride(UUID rideId, UUID driverId, UUID passengerId, RideStatus rideStatus) {
        return Ride.builder()
            .rideId(rideId)
            .driverId(driverId)
            .passengerId(passengerId)
            .startingCoordinates(Arrays.asList(40.7128, -74.0060))
            .endingCoordinates(Arrays.asList(34.0522, -118.2437))
            .rideStatus(rideStatus)
            .orderDateTime(LocalDateTime.now())
            .price(new BigDecimal("25.50"))
            .build();
    }

    public static RideResponse rideResponse(UUID rideId, UUID driverId, UUID passengerId, RideStatus rideStatus) {
        return new RideResponse(
            rideId,
            driverId,
            passengerId,
            Arrays.asList(40.7128, -74.0060),
            Arrays.asList(34.0522, -118.2437),
            rideStatus,
            LocalDateTime.now(),
            new BigDecimal("25.50"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static ShortRideResponse shortRideResponse(UUID rideId, UUID driverId, UUID passengerId) {
        return new ShortRideResponse(
            rideId,
            driverId,
            passengerId
        );
    }

    public static PagedRideResponse pagedRideResponse(UUID rideId, UUID driverId, UUID passengerId) {
        return new PagedRideResponse(0, 10, 1,
                List.of(rideResponse(rideId, driverId, passengerId, RideStatus.CREATED)));
    }

}
