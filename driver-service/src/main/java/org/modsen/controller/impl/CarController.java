package org.modsen.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modsen.controller.CarSwagger;
import org.modsen.dto.response.PagedResponse;
import org.modsen.dto.response.SuccessResponse;
import org.modsen.dto.car.CarRequest;
import org.modsen.dto.car.CarResponse;
import org.modsen.dto.request.RequestParams;
import org.modsen.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController implements CarApi {

    private final CarService carService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public PagedResponse<CarResponse> getAllCars(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "number") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        RequestParams requestParams = RequestParams.builder()
                .page(page)
                .limit(limit)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        return carService.getAllCars(requestParams);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SuccessResponse createCar(@Valid @RequestBody CarRequest carRequest) {
        UUID carId = carService.createCar(carRequest);
        return new SuccessResponse(carId.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{carId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CarResponse updateCar(@PathVariable UUID carId,
                                 @Valid @RequestBody CarRequest carRequest) {
        return carService.updateCar(carId, carRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{carId}", produces = APPLICATION_JSON_VALUE)
    public void deleteCar(@PathVariable UUID carId) {
        carService.deleteCar(carId);
    }

}
