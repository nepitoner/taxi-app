package org.modsen.validator.impl;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modsen.dto.driver.DriverRequest;
import org.modsen.exception.DriverNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.repository.DriverRepository;
import org.modsen.utils.constant.ExceptionConstant;
import org.modsen.validator.DriverValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverValidatorImpl implements DriverValidator {

    private final DriverRepository driverRepository;

    @Override
    public void checkUniqueness(DriverRequest dto) {
        if (driverRepository.existsByPhoneNumber(dto.phoneNumber())) {
            throw new RepeatedDataException(
                ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE.formatted(dto.phoneNumber()));
        }

        if (driverRepository.existsByEmail(dto.email())) {
            throw new RepeatedDataException(
                ExceptionConstant.REPEATED_EMAIL_MESSAGE.formatted(dto.email()));
        }
    }

    @Override
    public void checkUniqueness(UUID driverId, DriverRequest dto) {
        if (driverRepository.existsByPhoneNumberAndIdIsNot(dto.phoneNumber(), driverId)) {
            throw new RepeatedDataException(
                ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE.formatted(dto.phoneNumber()));
        }

        if (driverRepository.existsByEmailAndIdIsNot(dto.email(), driverId)) {
            throw new RepeatedDataException(
                ExceptionConstant.REPEATED_EMAIL_MESSAGE.formatted(dto.email()));
        }
    }

    @Override
    public void checkExistenceAndPresence(UUID driverId) {
        if (!driverRepository.existsByIdAndIsDeletedIsFalse(driverId)) {
            throw new DriverNotFoundException(
                ExceptionConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(driverId));
        }
    }

}
