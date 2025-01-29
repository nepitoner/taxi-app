package org.example.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.controller.DriverSwagger;
import org.example.dto.PagedResponse;
import org.example.dto.SuccessResponse;
import org.example.dto.driver.DriverRequest;
import org.example.dto.driver.DriverResponse;
import org.example.exception.RequestTimeoutException;
import org.example.service.DriverService;
import org.example.service.StorageService;
import org.example.validator.annotation.NotEmptyFile;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverController implements DriverSwagger {

    private final DriverService driverService;

    private final StorageService storageService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public PagedResponse<DriverResponse> getAllDrivers(@RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
                                       @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit) {
        return driverService.getAllDrivers(page, limit);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SuccessResponse registerDriver(@Valid @RequestBody DriverRequest driverRequest) {
        UUID registeredDriverId = driverService.registerDriver(driverRequest);
        return new SuccessResponse(registeredDriverId.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{driverId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public DriverResponse updateDriver(@PathVariable UUID driverId,
                                       @Valid @RequestBody DriverRequest driverRequest) {
        return driverService.updateDriver(driverId, driverRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{driverId}", produces = APPLICATION_JSON_VALUE)
    public void deleteDriver(@PathVariable UUID driverId) {
        driverService.deleteDriver(driverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/{driverId}/driver_photo", consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SuccessResponse addDriverPhoto(@PathVariable UUID driverId,
                                          @RequestPart(value = "photoFile") @NotEmptyFile MultipartFile photoFile) throws IOException, RequestTimeoutException {
        UUID id = storageService.saveFileReference(photoFile, driverId);
        return new SuccessResponse(id.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/{driverId}/{carId}", produces = APPLICATION_JSON_VALUE)
    public DriverResponse addCar(@PathVariable UUID driverId,
                                 @PathVariable UUID carId) {
        return driverService.addCar(driverId, carId);
    }

}
