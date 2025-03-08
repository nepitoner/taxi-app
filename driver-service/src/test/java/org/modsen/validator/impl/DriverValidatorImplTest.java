package org.modsen.validator.impl;


import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.modsen.util.TestUtil.driverRequest;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.dto.driver.DriverRequest;
import org.modsen.exception.DriverNotFoundException;
import org.modsen.exception.RepeatedDataException;
import org.modsen.repository.DriverRepository;

@ExtendWith(MockitoExtension.class)
class DriverValidatorImplTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverValidatorImpl driverValidator;

    private DriverRequest driverRequest;
    private UUID driverId;

    @BeforeEach
    void setUp() {
        driverRequest = driverRequest();
        driverId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Test checking uniqueness")
    void testCheckUniqueness() {
        when(driverRepository.existsByPhoneNumber(driverRequest.phoneNumber())).thenReturn(false);
        when(driverRepository.existsByEmail(driverRequest.email())).thenReturn(false);

        assertThatCode(() -> driverValidator.checkUniqueness(driverRequest))
            .doesNotThrowAnyException();

        verify(driverRepository).existsByPhoneNumber(driverRequest.phoneNumber());
        verify(driverRepository).existsByEmail(driverRequest.email());
    }

    @Test
    @DisplayName("Test checking uniqueness but phone number already exists")
    void testCheckUniqueness_DuplicatePhone() {
        when(driverRepository.existsByPhoneNumber(driverRequest.phoneNumber())).thenReturn(true);

        assertThatThrownBy(() -> driverValidator.checkUniqueness(driverRequest))
            .isInstanceOf(RepeatedDataException.class)
            .hasMessageContaining(driverRequest.phoneNumber());

        verify(driverRepository).existsByPhoneNumber(driverRequest.phoneNumber());
        verify(driverRepository, never()).existsByEmail(anyString());
    }

    @Test
    @DisplayName("Test checking uniqueness but email already exists")
    void testCheckUniqueness_DuplicateEmail() {
        when(driverRepository.existsByPhoneNumber(driverRequest.phoneNumber())).thenReturn(false);
        when(driverRepository.existsByEmail(driverRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> driverValidator.checkUniqueness(driverRequest))
            .isInstanceOf(RepeatedDataException.class)
            .hasMessageContaining(driverRequest.email());

        verify(driverRepository).existsByPhoneNumber(driverRequest.phoneNumber());
        verify(driverRepository).existsByEmail(driverRequest.email());
    }

    @Test
    @DisplayName("Test checking uniqueness for existing driver during update")
    void testCheckUniquenessForExistingDriver() {
        when(driverRepository.existsByPhoneNumberAndIdIsNot(driverRequest.phoneNumber(), driverId)).thenReturn(false);
        when(driverRepository.existsByEmailAndIdIsNot(driverRequest.email(), driverId)).thenReturn(false);

        assertThatCode(() -> driverValidator.checkUniqueness(driverId, driverRequest))
            .doesNotThrowAnyException();

        verify(driverRepository).existsByPhoneNumberAndIdIsNot(driverRequest.phoneNumber(), driverId);
        verify(driverRepository).existsByEmailAndIdIsNot(driverRequest.email(), driverId);
    }

    @Test
    @DisplayName("Test checking uniqueness for existing driver but phone number already exists")
    void testCheckUniquenessForExistingDriver_DuplicatePhone() {
        when(driverRepository.existsByPhoneNumberAndIdIsNot(driverRequest.phoneNumber(), driverId)).thenReturn(true);

        assertThatThrownBy(() -> driverValidator.checkUniqueness(driverId, driverRequest))
            .isInstanceOf(RepeatedDataException.class)
            .hasMessageContaining(driverRequest.phoneNumber());

        verify(driverRepository).existsByPhoneNumberAndIdIsNot(driverRequest.phoneNumber(), driverId);
        verify(driverRepository, never()).existsByEmailAndIdIsNot(anyString(), any());
    }

    @Test
    @DisplayName("Test checking uniqueness for existing driver but email already exists")
    void testCheckUniquenessForExistingDriver_DuplicateEmail() {
        when(driverRepository.existsByPhoneNumberAndIdIsNot(driverRequest.phoneNumber(), driverId)).thenReturn(false);
        when(driverRepository.existsByEmailAndIdIsNot(driverRequest.email(), driverId)).thenReturn(true);

        assertThatThrownBy(() -> driverValidator.checkUniqueness(driverId, driverRequest))
            .isInstanceOf(RepeatedDataException.class)
            .hasMessageContaining(driverRequest.email());

        verify(driverRepository).existsByPhoneNumberAndIdIsNot(driverRequest.phoneNumber(), driverId);
        verify(driverRepository).existsByEmailAndIdIsNot(driverRequest.email(), driverId);
    }

    @Test
    @DisplayName("Test checking existence")
    void testCheckExistenceAndPresence() {
        when(driverRepository.existsByIdAndIsDeletedIsFalse(driverId)).thenReturn(true);

        assertThatCode(() -> driverValidator.checkExistenceAndPresence(driverId))
            .doesNotThrowAnyException();

        verify(driverRepository).existsByIdAndIsDeletedIsFalse(driverId);
    }

    @Test
    @DisplayName("Test checking driver existence but driver doesn't exist")
    void testCheckExistenceAndPresenceDriver_NotFound() {
        when(driverRepository.existsByIdAndIsDeletedIsFalse(driverId)).thenReturn(false);

        assertThatThrownBy(() -> driverValidator.checkExistenceAndPresence(driverId))
            .isInstanceOf(DriverNotFoundException.class)
            .hasMessageContaining(driverId.toString());

        verify(driverRepository).existsByIdAndIsDeletedIsFalse(driverId);
    }

}