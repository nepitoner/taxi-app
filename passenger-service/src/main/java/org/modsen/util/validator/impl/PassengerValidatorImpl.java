package org.modsen.util.validator.impl;

import lombok.RequiredArgsConstructor;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.exception.PassengerNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.repository.PassengerRepository;
import org.modsen.util.constant.ExceptionConstant;
import org.modsen.util.validator.PassengerValidator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PassengerValidatorImpl implements PassengerValidator {

    private final PassengerRepository passengerRepository;

    @Override
    public void checkUniqueness(PassengerRequest dto) {
        if (passengerRepository.existsByPhoneNumber(dto.phoneNumber())) {
            throw new RepeatedDataException(
                    ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE.formatted(dto.phoneNumber()));
        }

        if (passengerRepository.existsByEmail(dto.email())) {
            throw new RepeatedDataException(
                    ExceptionConstant.REPEATED_EMAIL_MESSAGE.formatted(dto.email()));
        }
    }

    @Override
    public void checkUniqueness(UUID passengerId, PassengerRequest dto) {

        if (passengerRepository.existsByPhoneNumberAndPassengerIdIsNot(dto.phoneNumber(), passengerId)) {
            throw new RepeatedDataException(
                    ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE.formatted(dto.phoneNumber()));
        }

        if (passengerRepository.existsByEmailAndPassengerIdIsNot(dto.email(), passengerId)) {
            throw new RepeatedDataException(
                    ExceptionConstant.REPEATED_EMAIL_MESSAGE.formatted(dto.email()));
        }
    }

    @Override
    public void checkExistenceAndPresence(UUID passengerId) {
        if (!passengerRepository.existsByPassengerIdAndIsDeletedIsFalse(passengerId)) {
            throw new PassengerNotFoundException(
                    ExceptionConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(passengerId));
        }
    }

}
