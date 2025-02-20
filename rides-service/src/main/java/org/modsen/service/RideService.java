package org.modsen.service;

import org.modsen.dto.request.RideRequest;
import org.modsen.dto.request.RideRequestParams;
import org.modsen.dto.request.RideStatusRequest;
import org.modsen.dto.response.PagedRideResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.dto.response.ShortRideResponse;

import java.util.UUID;

public interface RideService {

    PagedRideResponse getAllRides(RideRequestParams requestParams);

    PagedRideResponse getAllRidesByDriverId(RideRequestParams requestParams, UUID driverId);

    PagedRideResponse getAllRidesByPassengerId(RideRequestParams requestParams, UUID passengerId);

    ShortRideResponse getRideById(UUID rideId);

    UUID createRide(RideRequest request);

    RideResponse updateRide(UUID rideId, RideRequest request);

    RideResponse changeRideStatus(UUID rideId, RideStatusRequest request);

}
