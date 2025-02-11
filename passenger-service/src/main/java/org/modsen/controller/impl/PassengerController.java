package org.modsen.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modsen.controller.PassengerSwagger;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedPassengerResponse;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.dto.response.SuccessResponse;
import org.modsen.exception.RequestTimeoutException;
import org.modsen.service.PassengerService;
import org.modsen.service.StorageService;
import org.modsen.util.validator.annotation.NotEmptyFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passengers")
public class PassengerController implements PassengerSwagger {

    private final PassengerService passengerService;

    private final StorageService storageService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedPassengerResponse> getAllPassengers(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        RequestParams requestParams = RequestParams.builder()
                .page(page)
                .limit(limit)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        PagedPassengerResponse passengers = passengerService.getAllPassengers(requestParams);
        return ResponseEntity.status(HttpStatus.OK).body(passengers);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> registerPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        UUID registeredPassengerId = passengerService.registerPassenger(passengerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(registeredPassengerId.toString()));
    }

    @PutMapping(value = "/{passengerId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable UUID passengerId,
                                                             @Valid @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse passengerResponse = passengerService.updatePassenger(passengerId, passengerRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(passengerResponse);
    }

    @DeleteMapping(value = "/{passengerId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePassenger(@PathVariable UUID passengerId) {
        passengerService.deletePassenger(passengerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{passengerId}/passenger_photo", consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> addPassengerPhoto(@PathVariable UUID passengerId,
                                                             @RequestPart(value = "photoFile")
                                                             @NotEmptyFile MultipartFile photoFile) throws IOException, RequestTimeoutException {
        UUID id = storageService.saveFileReference(photoFile, passengerId);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(id.toString()));
    }

}
