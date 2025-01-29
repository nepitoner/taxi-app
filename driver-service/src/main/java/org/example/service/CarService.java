package org.example.service;

import org.example.dto.PagedResponse;
import org.example.dto.car.CarRequest;
import org.example.dto.car.CarResponse;

import java.util.UUID;

public interface CarService {

    UUID registerCar(CarRequest carRequest);

    CarResponse updateCar(UUID carId, CarRequest carRequest);

    void deleteCar(UUID carId);

    PagedResponse<CarResponse> getAllCars(int page, int limit);

    CarResponse getCarById(UUID carId);

}
