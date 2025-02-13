package org.modsen.controller.impl;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modsen.controller.RideApi;
import org.modsen.dto.request.RideRequest;
import org.modsen.dto.request.RideRequestParams;
import org.modsen.dto.request.RideStatusRequest;
import org.modsen.dto.response.PagedRideResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.dto.response.ShortRideResponse;
import org.modsen.dto.response.SuccessResponse;
import org.modsen.service.RideService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rides")
public class RideController implements RideApi {

    private final RideService rideService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public PagedRideResponse getAllRides(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        RideRequestParams requestParams = RideRequestParams.builder()
                .page(page)
                .limit(limit)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        return rideService.getAllRides(requestParams);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/drivers/{driverId}", produces = APPLICATION_JSON_VALUE)
    public PagedRideResponse getAllRidesByDriverId(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable UUID driverId) {
        RideRequestParams requestParams = RideRequestParams.builder()
                .page(page)
                .limit(limit)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        return rideService.getAllRidesByDriverId(requestParams, driverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/passengers/{passengerId}", produces = APPLICATION_JSON_VALUE)
    public PagedRideResponse getAllRidesByPassengerId(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable UUID passengerId) {
        RideRequestParams requestParams = RideRequestParams.builder()
                .page(page)
                .limit(limit)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        return rideService.getAllRidesByPassengerId(requestParams, passengerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{rideId}/{participantId}", produces = APPLICATION_JSON_VALUE)
    public ShortRideResponse getRideByIdWithParticipantExistenceCheck(
            @PathVariable UUID rideId,
            @PathVariable UUID participantId) {
        return rideService.getRideByIdWithParticipantExistenceCheck(rideId, participantId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SuccessResponse createRide(@Valid @RequestBody RideRequest request) {
        UUID createdRideId = rideService.createRide(request);
        return new SuccessResponse(createdRideId.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{rideId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RideResponse updateRide(@PathVariable UUID rideId,
                                   @Valid @RequestBody RideRequest request) {
        return rideService.updateRide(rideId, request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{rideId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RideResponse changeRideStatus(@PathVariable UUID rideId,
                                         @Valid @RequestBody RideStatusRequest request) {
        return rideService.changeRideStatus(rideId, request);
    }

}
