package org.modsen.controller.impl;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modsen.controller.DriverApi;
import org.modsen.dto.driver.DriverRequest;
import org.modsen.dto.driver.DriverResponse;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedResponse;
import org.modsen.dto.response.SuccessResponse;
import org.modsen.exception.RequestTimeoutException;
import org.modsen.service.DriverService;
import org.modsen.service.StorageService;
import org.modsen.validator.annotation.NotEmptyFile;
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


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverController implements DriverApi {

    private final DriverService driverService;

    private final StorageService storageService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public PagedResponse<DriverResponse> getAllDrivers(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
        @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDirection) {

        RequestParams requestParams = RequestParams.builder()
            .page(page)
            .limit(limit)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();
        return driverService.getAllDrivers(requestParams);
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
                                          @RequestPart(value = "photoFile")
                                          @NotEmptyFile MultipartFile photoFile)
        throws IOException, RequestTimeoutException {
        UUID id = storageService.saveFileReference(photoFile, driverId);
        return new SuccessResponse(id.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/{driverId}/{carId}", produces = APPLICATION_JSON_VALUE)
    public DriverResponse addCar(@PathVariable UUID driverId,
                                 @PathVariable UUID carId) {
        return driverService.addCar(driverId, carId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/available/{driverId}")
    public void changeDriverAvailableStatus(@PathVariable UUID driverId) {
        driverService.changeDriverAvailableStatus(driverId);
    }

}
