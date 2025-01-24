package org.example.facade;

import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;
import org.example.exception.RequestTimeoutException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PassengerFacade {

    UUID validateNewPassenger(PassengerRequest dto);

    PassengerResponse validateRegisteredPassenger(UUID passengerId, PassengerRequest dto);

    void validateNotDeletedPassenger(UUID passengerId);

    UUID sendPhotoIntoStorage(MultipartFile photoFile, UUID passengerId) throws IOException, RequestTimeoutException;

}
