package org.example.facade;

import org.example.dto.PassengerDtoRequest;
import org.example.dto.PassengerDtoResponse;
import org.example.exception.RequestTimeoutException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PassengerFacade {

    UUID validateNewPassenger(PassengerDtoRequest dto);

    PassengerDtoResponse validateRegisteredPassenger(UUID passengerId, PassengerDtoRequest dto);

    void validateNotDeletedPassenger(UUID passengerId);

    UUID sendPhotoIntoStorage(MultipartFile photoFile, UUID passengerId) throws IOException, RequestTimeoutException;

}
