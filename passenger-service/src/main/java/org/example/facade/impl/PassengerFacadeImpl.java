package org.example.facade.impl;

import lombok.RequiredArgsConstructor;
import org.example.config.MinioConfigProperties;
import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;
import org.example.exception.PassengerNotFoundException;
import org.example.exception.RepeatedDataException;
import org.example.exception.RequestTimeoutException;
import org.example.facade.PassengerFacade;
import org.example.repository.PassengerRepository;
import org.example.service.PassengerService;
import org.example.service.StorageService;
import org.example.utils.constant.ExceptionConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PassengerFacadeImpl implements PassengerFacade {

    private final StorageService storageService;

    private final PassengerService passengerService;

    private final MinioConfigProperties properties;

    private final PassengerRepository passengerRepository;

    @Override
    public UUID validateNewPassenger(PassengerRequest dto) {
        if (passengerRepository.existsByPhoneNumberOrEmail(dto.phoneNumber(), dto.email())) {
            throw new RepeatedDataException(String.format(
                    ExceptionConstant.REPEATED_DATA_MESSAGE, dto.phoneNumber(), dto.email()));
        }
        return passengerService.registerPassenger(dto);
    }

    @Override
    public PassengerResponse validateRegisteredPassenger(UUID passengerId, PassengerRequest dto) {
        checkExistenceAndPresence(passengerId);
        if (passengerRepository.existsByPhoneNumberAndPassengerIdIsNot(dto.phoneNumber(), passengerId) ||
                passengerRepository.existsByEmailAndPassengerIdIsNot(dto.email(), passengerId)
        ) {
            throw new RepeatedDataException(String.format(
                    ExceptionConstant.REPEATED_DATA_MESSAGE, dto.phoneNumber(), dto.email()));
        }
        return passengerService.updatePassenger(passengerId, dto);
    }

    @Override
    public void validateNotDeletedPassenger(UUID passengerId) {
        checkExistenceAndPresence(passengerId);
        passengerService.deletePassenger(passengerId);
    }

    @Override
    public UUID sendPhotoIntoStorage(MultipartFile photoFile, UUID passengerId) throws IOException, RequestTimeoutException {
        checkExistenceAndPresence(passengerId);
        String fileRef = storageService.uploadFile(properties.bucket().getPhotoBucketName(),
                "passenger_photo_" + passengerId, photoFile.getInputStream(),
                photoFile.getContentType());
        return passengerService.addPhoto(passengerId, fileRef);
    }

    private void checkExistenceAndPresence(UUID passengerId) {
        if (!passengerRepository.existsByPassengerIdAndIsDeletedIsFalse(passengerId)) {
            throw new PassengerNotFoundException(String.format(ExceptionConstant.NOT_FOUND_MESSAGE, passengerId));
        }
    }

}
