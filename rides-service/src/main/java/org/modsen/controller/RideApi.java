package org.modsen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.modsen.dto.request.RideRequest;
import org.modsen.dto.request.RideStatusRequest;
import org.modsen.dto.response.PagedRideResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.dto.response.ShortRideResponse;
import org.modsen.dto.response.SuccessResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@Tag(name = "Ride", description = "Methods for managing rides")
public interface RideApi {

    @Operation(summary = "Getting paged rides")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the rides were successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    PagedRideResponse getAllRides(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    );

    @Operation(description = "Getting paged rides by driver id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the rides were successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    PagedRideResponse getAllRidesByDriverId(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable UUID driverId
    );

    @Operation(description = "Getting paged rides by passenger id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the rides were successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    PagedRideResponse getAllRidesByPassengerId(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable UUID passengerId
    );

    @Operation(description = "Getting the ride by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The ride was successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "The ride wasn't found"),
    })
    ShortRideResponse getRideById(@PathVariable UUID rideId);

    @Operation(summary = "Create a new ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The ride was successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    SuccessResponse createRide(@Valid @RequestBody RideRequest request);

    @Operation(summary = "Update the ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The ride was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Ride with specified id wasn't found")
    })
    RideResponse updateRide(@PathVariable UUID rideId,
                            @Valid @RequestBody RideRequest request);

    @Operation(summary = "Change ride's status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The ride's status was successfully changed"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Ride with specified id wasn't found")
    })
    RideResponse changeRideStatus(@PathVariable UUID rideId,
                                  @Valid @RequestBody RideStatusRequest request);

}
