package org.example.service;

import org.example.dto.PagedResponse;
import org.example.dto.driver.DriverRequest;
import org.example.dto.driver.DriverResponse;

import java.util.UUID;

public interface DriverService {

    UUID registerDriver(DriverRequest driverRequest);

    DriverResponse updateDriver(UUID driverId, DriverRequest driverRequest);

    UUID addPhoto(UUID driverId, String fileRef);

    void deleteDriver(UUID driverId);

    PagedResponse<DriverResponse> getAllDrivers(int page, int limit);

    DriverResponse addCar(UUID driverId, UUID carId);
}
