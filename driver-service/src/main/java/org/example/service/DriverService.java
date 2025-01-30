package org.example.service;

import org.example.dto.PagedResponse;
import org.example.dto.driver.DriverRequest;
import org.example.dto.driver.DriverResponse;

import java.util.UUID;

public interface DriverService {

    PagedResponse<DriverResponse> getAllDrivers(int page, int limit);

    UUID registerDriver(DriverRequest driverRequest);

    DriverResponse updateDriver(UUID driverId, DriverRequest driverRequest);

    void deleteDriver(UUID driverId);

    UUID addPhoto(UUID driverId, String fileRef);

    DriverResponse addCar(UUID driverId, UUID carId);
}
