package org.modsen.util.validator.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.modsen.util.constant.ExceptionConstant.PASSENGER_NOT_FOUND_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_EMAIL_MESSAGE;
import static org.modsen.util.constant.ExceptionConstant.REPEATED_PHONE_NUMBER_MESSAGE;
import static org.modsen.util.TestUtil.passengerDtoRequest;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.exception.PassengerNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.repository.PassengerRepository;
import org.modsen.validator.impl.PassengerValidatorImpl;

@ExtendWith(MockitoExtension.class)
class PassengerValidatorImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerValidatorImpl passengerValidator;

    private UUID passengerId;

    @BeforeEach
    void setUp() {
        passengerId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Check uniqueness of passenger's phone number and email")
    void testCheckUniqueness() {
        PassengerRequest request = passengerDtoRequest();

        when(passengerRepository.existsByPhoneNumber(request.phoneNumber())).thenReturn(false);
        when(passengerRepository.existsByEmail(request.email())).thenReturn(false);

        assertDoesNotThrow(() -> passengerValidator.checkUniqueness(request));
    }

    @Test
    @DisplayName("Test throwing RepeatedDataException for passenger duplicate phone number")
    void testCheckUniqueness_DuplicatePhoneNumber() {
        PassengerRequest request = passengerDtoRequest();

        when(passengerRepository.existsByPhoneNumber(request.phoneNumber())).thenReturn(true);

        RepeatedDataException exception = assertThrows(RepeatedDataException.class,
            () -> passengerValidator.checkUniqueness(request));

        assertEquals(REPEATED_PHONE_NUMBER_MESSAGE.formatted(request.phoneNumber()), exception.getMessage());
    }

    @Test
    @DisplayName("Test throwing RepeatedDataException for passenger duplicate email")
    void testCheckUniqueness_DuplicateEmail() {
        PassengerRequest request = passengerDtoRequest();

        when(passengerRepository.existsByEmail(request.email())).thenReturn(true);

        RepeatedDataException exception = assertThrows(RepeatedDataException.class,
            () -> passengerValidator.checkUniqueness(request));

        assertEquals(REPEATED_EMAIL_MESSAGE.formatted(request.email()), exception.getMessage());
    }

    @Test
    @DisplayName("Check uniqueness by passenger id")
    void testCheckUniquenessById() {
        PassengerRequest request = passengerDtoRequest();

        when(passengerRepository.existsByPhoneNumberAndPassengerIdIsNot(request.phoneNumber(), passengerId))
            .thenReturn(false);
        when(passengerRepository.existsByEmailAndPassengerIdIsNot(request.email(), passengerId)).thenReturn(false);

        assertDoesNotThrow(() -> passengerValidator.checkUniqueness(passengerId, request));
    }

    @Test
    @DisplayName("Test throwing RepeatedDataException for passenger's duplicate phone number and his id")
    void testCheckUniquenessById_DuplicatePhoneNumber() {
        PassengerRequest request = passengerDtoRequest();

        when(passengerRepository.existsByPhoneNumberAndPassengerIdIsNot(request.phoneNumber(), passengerId))
            .thenReturn(true);

        RepeatedDataException exception = assertThrows(RepeatedDataException.class,
            () -> passengerValidator.checkUniqueness(passengerId, request));

        assertEquals(REPEATED_PHONE_NUMBER_MESSAGE.formatted(request.phoneNumber()), exception.getMessage());
    }

    @Test
    @DisplayName("Test throwing RepeatedDataException for passengers duplicate email and his id")
    void testCheckUniquenessById_DuplicateEmail() {
        PassengerRequest request = passengerDtoRequest();

        when(passengerRepository.existsByEmailAndPassengerIdIsNot(request.email(), passengerId)).thenReturn(true);

        RepeatedDataException exception = assertThrows(RepeatedDataException.class,
            () -> passengerValidator.checkUniqueness(passengerId, request));

        assertEquals(REPEATED_EMAIL_MESSAGE.formatted(request.email()), exception.getMessage());
    }

    @Test
    @DisplayName("Check existence and presence of the passenger")
    void testCheckExistenceAndPresence() {
        when(passengerRepository.existsByPassengerIdAndIsDeletedIsFalse(passengerId)).thenReturn(true);

        assertDoesNotThrow(() -> passengerValidator.checkExistenceAndPresence(passengerId));
    }

    @Test
    @DisplayName("Test throwing PassengerNotFoundException when passenger not found")
    void testCheckExistenceAndPresence_NotFound() {
        when(passengerRepository.existsByPassengerIdAndIsDeletedIsFalse(passengerId)).thenReturn(false);

        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class,
            () -> passengerValidator.checkExistenceAndPresence(passengerId));

        assertEquals(PASSENGER_NOT_FOUND_MESSAGE.formatted(passengerId), exception.getMessage());
    }

}