package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PagedResponse;
import org.example.dto.car.CarRequest;
import org.example.dto.car.CarResponse;
import org.example.entity.Car;
import org.example.mapper.CarMapper;
import org.example.repository.CarRepository;
import org.example.service.CarService;
import org.example.validator.CarValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarMapper carMapper;

    private final CarValidator carValidator;

    private final CarRepository carRepository;

    @Override
    public CarResponse getCarById(UUID carId) {
        carValidator.checkExistenceAndPresence(carId);
        Car car = carRepository.findByIdAndIsDeletedIsFalse(carId);
        log.info("Car Service. Get car by id {}", carId);
        return carMapper.mapEntityToResponse(car);
    }

    @Override
    public PagedResponse<CarResponse> getAllCars(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Car> responsePage = carRepository.findByIsDeletedIsFalse(pageable);
        PagedResponse<CarResponse> pagedCarResponse = carMapper.mapPageEntityToPagedDto(page, limit, responsePage);
        log.info("Car Service. Get all cars. Total cars {}", pagedCarResponse.totalAmount());
        return pagedCarResponse;
    }

    @Override
    @Transactional
    public UUID createCar(CarRequest dto) {
        carValidator.checkUniqueness(dto);

        Car carToRegister = carMapper.mapDtoToEntity(dto);
        UUID carId = carRepository.save(carToRegister).getId();

        log.info("Car Service. Create car with number {}. Created car id {}",
                carToRegister.getNumber(), carId);
        return carId;
    }

    @Override
    @Transactional
    public CarResponse updateCar(UUID carId, CarRequest carRequest) {
        carValidator.checkExistenceAndPresence(carId);
        carValidator.checkUniqueness(carId, carRequest);
        Car car = carRepository.findByIdAndIsDeletedIsFalse(carId);

        Car updateCar = carMapper.mapDtoToEntity(carRequest, car);

        Car newCar = carRepository.save(updateCar);
        log.info("Car Service. Update car with id {}", carId);
        return carMapper.mapEntityToResponse(newCar);
    }

    @Override
    @Transactional
    public void deleteCar(UUID carId) {
        carValidator.checkExistenceAndPresence(carId);

        Car car = carRepository.findByIdAndIsDeletedIsFalse(carId);
        car.setIsDeleted(true);
        car.getDrivers().clear();
        carRepository.save(car);
        log.info("Car Service. Delete car with id {}", carId);
    }

}
