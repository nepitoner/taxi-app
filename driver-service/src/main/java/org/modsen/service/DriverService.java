package org.modsen.service;

import org.modsen.dto.response.PagedResponse;
import org.modsen.dto.driver.DriverRequest;
import org.modsen.dto.driver.DriverResponse;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.RateResponse;

import java.util.UUID;

public interface DriverService {

    PagedResponse<DriverResponse> getAllDrivers(RequestParams requestParams);

    UUID registerDriver(DriverRequest driverRequest);

    DriverResponse updateDriver(UUID driverId, DriverRequest driverRequest);

    void deleteDriver(UUID driverId);

    UUID addPhoto(UUID driverId, String fileRef);

    DriverResponse addCar(UUID driverId, UUID carId);

    void updateDriverRating(RateResponse rateResponse);

}
