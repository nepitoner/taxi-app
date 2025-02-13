package org.modsen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.modsen.dto.response.PagedResponse;
import org.modsen.dto.response.SuccessResponse;
import org.modsen.dto.car.CarRequest;
import org.modsen.dto.car.CarResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;


@Tag(name = "Car", description = "Methods for managing cars activity")
public interface CarSwagger {

    @Operation(summary = "Getting all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the cars were successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    PagedResponse<CarResponse> getAllCars(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection);

    @Operation(summary = "Register new car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The car was successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    SuccessResponse createCar(@Valid @RequestBody CarRequest carRequest);

    @Operation(summary = "Update the car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The car was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Car with specified id wasn't found")
    })
    CarResponse updateCar(@PathVariable UUID carId,
                          @Valid @RequestBody CarRequest carRequest);

    @Operation(summary = "Delete the car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The car was successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Car with specified id wasn't found")
    })
    void deleteCar(@PathVariable UUID carId);

}
