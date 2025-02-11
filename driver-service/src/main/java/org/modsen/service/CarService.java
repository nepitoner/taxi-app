package org.modsen.service;

import org.modsen.dto.PagedResponse;
import org.modsen.dto.car.CarRequest;
import org.modsen.dto.car.CarResponse;
import org.modsen.dto.request.RequestParams;

import java.util.UUID;

public interface CarService {

    CarResponse getCarById(UUID carId);

    PagedResponse<CarResponse> getAllCars(RequestParams requestParams);

    UUID createCar(CarRequest carRequest);

    CarResponse updateCar(UUID carId, CarRequest carRequest);

    void deleteCar(UUID carId);

}
