package org.modsen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.response.PagedResponse;
import org.modsen.dto.driver.DriverRequest;
import org.modsen.dto.driver.DriverResponse;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.RateResponse;
import org.modsen.entity.Car;
import org.modsen.entity.Driver;
import org.modsen.exception.CarAlreadyTakenException;
import org.modsen.mapper.CarMapper;
import org.modsen.mapper.DriverMapper;
import org.modsen.repository.CarRepository;
import org.modsen.repository.DriverRepository;
import org.modsen.service.CarService;
import org.modsen.service.DriverService;
import org.modsen.validator.DriverValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.modsen.utils.constant.ExceptionConstant.TAKEN_CAR_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final Clock clock;

    private final DriverMapper driverMapper;

    private final CarMapper carMapper;

    private final CarService carService;

    private final CarRepository carRepository;

    private final DriverValidator driverValidator;

    private final DriverRepository driverRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DriverResponse> getAllDrivers(RequestParams requestParams) {
        int limit = Math.min(requestParams.limit(), 50);
        Sort sort = Sort.by(Sort.Direction.fromString(requestParams.sortDirection()), requestParams.sortBy());
        Pageable pageable = PageRequest.of(requestParams.page(), limit, sort);

        Page<Driver> responsePage = driverRepository.findByIsDeletedIsFalse(pageable);
        PagedResponse<DriverResponse> pagedResponse = driverMapper.mapPageEntityToPagedDto(
                requestParams.page(), limit, responsePage);

        log.info("Driver Service. Get all drivers. Total drivers {}", pagedResponse.totalAmount());
        return pagedResponse;
    }

    @Override
    @Transactional
    public UUID registerDriver(DriverRequest dto) {
        driverValidator.checkUniqueness(dto);
        Driver driverToRegister = driverMapper.mapDtoToEntity(dto);
        UUID driverId = driverRepository.save(driverToRegister).getId();

        log.info("Driver Service. Register driver with email {}. Registered driver id {}",
                driverToRegister.getEmail(), driverId);
        return driverId;
    }

    @Override
    @Transactional
    public DriverResponse updateDriver(UUID driverId, DriverRequest driverRequest) {
        driverValidator.checkExistenceAndPresence(driverId);
        driverValidator.checkUniqueness(driverId, driverRequest);

        Driver driver = driverRepository.findByIdAndIsDeletedIsFalse(driverId);
        Driver updateDriver = driverMapper.mapDtoToEntity(driverRequest, LocalDateTime.now(clock), driver);

        Driver newDriver = driverRepository.save(updateDriver);
        log.info("Driver Service. Update driver with id {}", driverId);
        return driverMapper.mapEntityToResponse(newDriver);
    }

    @Override
    @Transactional
    public void deleteDriver(UUID driverId) {
        driverValidator.checkExistenceAndPresence(driverId);

        Driver driver = driverRepository.findByIdAndIsDeletedIsFalse(driverId);
        driver.getCars().forEach(car -> car.getDrivers().remove(driver));
        driver.setIsDeleted(true);
        driverRepository.save(driver);
        log.info("Driver Service. Delete driver with id {}", driverId);
    }

    @Override
    @Transactional
    public UUID addPhoto(UUID driverId, String fileRef) {
        driverValidator.checkExistenceAndPresence(driverId);

        Driver driver = driverRepository.findByIdAndIsDeletedIsFalse(driverId);
        driver.setProfilePictureRef(fileRef);
        log.info("Driver Service. Add photo to driver with id {}", driverId);
        return driverRepository.save(driver).getId();
    }

    @Override
    @Transactional
    public DriverResponse addCar(UUID driverId, UUID carId) {
        driverValidator.checkExistenceAndPresence(driverId);
        Driver driver = driverRepository.findByIdAndIsDeletedIsFalse(driverId);
        Car car = carMapper.mapResponseToEntity(carService.getCarById(carId));

        if (car.getDrivers().isEmpty()) {
            driver.getCars().add(car);
            car.getDrivers().add(driver);
        } else {
            throw new CarAlreadyTakenException(TAKEN_CAR_MESSAGE.formatted(carId));
        }

        Driver driverWithCar = driverRepository.save(driver);
        carRepository.save(car);

        log.info("Driver Service. Add car. Car id {} and driver id {}", carId, driverId);
        return driverMapper.mapEntityToResponse(driverWithCar);
    }

    @Override
    public void updateDriverRating(RateResponse rateResponse) {
        UUID driverId = UUID.fromString(rateResponse.toId());
        driverValidator.checkExistenceAndPresence(driverId);

        Driver driver = driverRepository.findByIdAndIsDeletedIsFalse(driverId);
        driver.setRating(rateResponse.rating());
        driverRepository.save(driver);

        log.info("Driver Service. Update rating. Driver id {}", rateResponse.toId());
    }

}
