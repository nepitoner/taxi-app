package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.example.dto.PagedResponse;
import org.example.dto.SuccessResponse;
import org.example.dto.driver.DriverRequest;
import org.example.dto.driver.DriverResponse;
import org.example.exception.RequestTimeoutException;
import org.example.validator.annotation.NotEmptyFile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "Driver", description = "Methods for managing drivers activity")
public interface DriverSwagger {
    @Operation(summary = "Getting all drivers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the drivers were successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    PagedResponse<DriverResponse> getAllDrivers(@RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
                                                @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit);

    @Operation(summary = "Register new driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The driver was successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    SuccessResponse registerDriver(@Valid @RequestBody DriverRequest driverRequest);

    @Operation(summary = "Update the driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The driver was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Driver with specified id wasn't found")
    })
    DriverResponse updateDriver(@PathVariable UUID driverId,
                                @Valid @RequestBody DriverRequest driverRequest);

    @Operation(summary = "Delete the driver by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The driver was successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Driver with specified id wasn't found")
    })
    void deleteDriver(@PathVariable UUID driverId);

    @Operation(summary = "Add driver's photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver's photo wad successfully added"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    SuccessResponse addDriverPhoto(@PathVariable UUID driverId,
                                   @RequestPart(value = "photoFile.name") @NotEmptyFile MultipartFile photoFile) throws IOException, RequestTimeoutException;

    @Operation(summary = "Add driver for the car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The driver was successfully added"),
            @ApiResponse(responseCode = "404", description = "Driver with specified id wasn't found")
    })
    DriverResponse addCar(@PathVariable UUID driverId,
                          @PathVariable UUID carId);
}
