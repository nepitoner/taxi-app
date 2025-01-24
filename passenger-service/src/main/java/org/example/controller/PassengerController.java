package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;
import org.example.dto.SuccessResponse;
import org.example.exception.RequestTimeoutException;
import org.example.facade.PassengerFacade;
import org.example.service.PassengerService;
import org.example.utils.validator.annotation.NotEmptyFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passengers")
@Tag(name = "Passenger", description = "Methods for managing passengers activity")
public class PassengerController {

    private final PassengerService passengerService;

    private final PassengerFacade passengerFacade;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Getting all passengers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the passengers were successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<List<PassengerResponse>> getAllPassengers(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Incorrect page. Must be greater than 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Incorrect data. Must be greater than 1") int limit
    ) {
        List<PassengerResponse> passengers = passengerService.getAllPassengers(page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(passengers);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Register new passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The passenger was successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<SuccessResponse> registerPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        UUID registeredPassengerId = passengerFacade.validateNewPassenger(passengerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(registeredPassengerId));
    }

    @PutMapping(value = "/{passengerId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update the passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The passenger was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Passenger with specified id wasn't found")
    })
    public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable UUID passengerId,
                                                             @Valid @RequestBody PassengerRequest passengerRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(passengerFacade.validateRegisteredPassenger(passengerId, passengerRequest));
    }

    @DeleteMapping(value = "/{passengerId}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete the passenger by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The passenger was successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<Void> deletePassenger(@PathVariable UUID passengerId) {
        passengerFacade.validateNotDeletedPassenger(passengerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{passengerId}/passenger_photo", consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Add passenger's photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger's photo wad successfully added"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<SuccessResponse> addPassengerPhoto(@PathVariable UUID passengerId,
                                                             @RequestPart(value = "photoFile") @NotEmptyFile MultipartFile photoFile) throws IOException, RequestTimeoutException {
        UUID id = passengerFacade.sendPhotoIntoStorage(photoFile, passengerId);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(id));
    }

}
