package org.modsen.service;

import java.io.IOException;
import java.util.UUID;
import org.modsen.dto.driver.DriverRequest;
import org.modsen.dto.driver.DriverResponse;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedResponse;
import org.modsen.dto.response.RateResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DriverService {

    PagedResponse<DriverResponse> getAllDrivers(RequestParams requestParams);

    UUID registerDriver(DriverRequest driverRequest);

    DriverResponse updateDriver(UUID driverId, DriverRequest driverRequest);

    void deleteDriver(UUID driverId);

    UUID addPhoto(MultipartFile photoFile, UUID driverId) throws IOException;

    DriverResponse addCar(UUID driverId, UUID carId);

    void updateDriverRating(RateResponse rateResponse);

    void changeDriverAvailableStatus(UUID driverId);

}
