package org.modsen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.modsen.dto.response.PagedPassengerResponse;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.dto.response.SuccessResponse;
import org.modsen.exception.RequestTimeoutException;
import org.modsen.util.validator.annotation.NotEmptyFile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Validated
@Tag(name = "Passenger", description = "Methods for managing passengers activity")
public interface PassengerApi {

    @Operation(summary = "Getting all passengers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the passengers were successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    ResponseEntity<PagedPassengerResponse> getAllPassengers(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    );

    @Operation(summary = "Register new passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The passenger was successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    ResponseEntity<SuccessResponse> registerPassenger(@Valid @RequestBody PassengerRequest passengerRequest);

    @Operation(summary = "Update the passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The passenger was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Passenger with specified id wasn't found")
    })
    ResponseEntity<PassengerResponse> updatePassenger(@PathVariable UUID passengerId,
                                                      @Valid @RequestBody PassengerRequest passengerRequest);

    @Operation(summary = "Delete the passenger by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The passenger was successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Passenger with specified id wasn't found")
    })
    ResponseEntity<Void> deletePassenger(@PathVariable UUID passengerId);

    @Operation(summary = "Add passenger's photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger's photo wad successfully added"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    ResponseEntity<SuccessResponse> addPassengerPhoto(@PathVariable UUID passengerId,
                                                      @RequestPart(value = "photoFile")
                                                      @NotEmptyFile MultipartFile photoFile) throws IOException, RequestTimeoutException;

}
