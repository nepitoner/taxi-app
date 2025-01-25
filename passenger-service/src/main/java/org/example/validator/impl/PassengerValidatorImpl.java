package org.example.validator.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.PassengerRequest;
import org.example.exception.PassengerNotFoundException;
import org.example.exception.RepeatedDataException;
import org.example.validator.PassengerValidator;
import org.example.repository.PassengerRepository;
import org.example.utils.constant.ExceptionConstant;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PassengerValidatorImpl implements PassengerValidator {

    private final PassengerRepository passengerRepository;

    @Override
    public void checkUniqueness(PassengerRequest dto) {
        if (passengerRepository.existsByPhoneNumber(dto.phoneNumber())) {
            throw new RepeatedDataException(String.format(
                    ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE, dto.phoneNumber()));
        }

        if (passengerRepository.existsByEmail(dto.email())) {
            throw new RepeatedDataException(String.format(
                    ExceptionConstant.REPEATED_EMAIL_MESSAGE, dto.email()));
        }
    }

    @Override
    public void checkUniqueness(UUID passengerId, PassengerRequest dto) {

        if (passengerRepository.existsByPhoneNumberAndPassengerIdIsNot(dto.phoneNumber(), passengerId)) {
            throw new RepeatedDataException(String.format(
                    ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE, dto.phoneNumber()));
        }

        if (passengerRepository.existsByEmailAndPassengerIdIsNot(dto.email(), passengerId)) {
            throw new RepeatedDataException(String.format(
                    ExceptionConstant.REPEATED_EMAIL_MESSAGE, dto.email()));
        }
    }

    @Override
    public void checkExistenceAndPresence(UUID passengerId) {
        if (!passengerRepository.existsByPassengerIdAndIsDeletedIsFalse(passengerId)) {
            throw new PassengerNotFoundException(
                    String.format(ExceptionConstant.PASSENGER_NOT_FOUND_MESSAGE, passengerId));
        }
    }

}
