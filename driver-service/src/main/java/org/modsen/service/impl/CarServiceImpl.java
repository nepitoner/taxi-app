package org.modsen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.response.PagedResponse;
import org.modsen.dto.car.CarRequest;
import org.modsen.dto.car.CarResponse;
import org.modsen.dto.request.RequestParams;
import org.modsen.entity.Car;
import org.modsen.mapper.CarMapper;
import org.modsen.repository.CarRepository;
import org.modsen.service.CarService;
import org.modsen.validator.CarValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Transactional(readOnly = true)
    public CarResponse getCarById(UUID carId) {
        carValidator.checkExistenceAndPresence(carId);
        Car car = carRepository.findByIdAndIsDeletedIsFalse(carId);
        log.info("Car Service. Get car by id {}", carId);
        return carMapper.mapEntityToResponse(car);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CarResponse> getAllCars(RequestParams requestParams) {
        int limit = Math.min(requestParams.limit(), 50);
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        Pageable pageable = PageRequest.of(requestParams.page(), limit, sort);

        Page<Car> responsePage = carRepository.findByIsDeletedIsFalse(pageable);
        PagedResponse<CarResponse> pagedCarResponse = carMapper.mapPageEntityToPagedDto(
                requestParams.page(), limit, responsePage);

        log.info("Car Service. Get all cars. Total cars {}", pagedCarResponse.totalAmount());
        return pagedCarResponse;
    }

    @Override
    @Transactional
    public UUID createCar(CarRequest dto) {
        carValidator.checkUniqueness(dto);

        Car carToCreate = carMapper.mapDtoToEntity(dto);
        UUID carId = carRepository.save(carToCreate).getId();

        log.info("Car Service. Create car with number {}. Created car id {}",
                carToCreate.getNumber(), carId);
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
