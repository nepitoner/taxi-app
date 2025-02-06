package org.example.service;

import org.example.dto.request.RideRequest;
import org.example.dto.request.RideStatusRequest;
import org.example.dto.response.PagedRideResponse;
import org.example.dto.response.RideResponse;
import java.util.UUID;

public interface RideService {

    PagedRideResponse getAllRides(int page, int limit);

    PagedRideResponse getAllRidesByDriverId(int page, int limit, UUID driverId);

    PagedRideResponse getAllRidesByPassengerId(int page, int limit, UUID passengerId);

    UUID createRide(RideRequest request);

    RideResponse updateRide(UUID rideId, RideRequest request);

    RideResponse changeRideStatus(UUID rideId, RideStatusRequest request);

}
