package org.modsen.service;

import java.io.IOException;
import java.util.UUID;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedPassengerResponse;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.dto.response.PassengerWithRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PassengerService {

    PagedPassengerResponse getAllPassengers(RequestParams requestParams);

    PassengerWithRatingResponse getPassengerById(UUID passengerId);

    UUID registerPassenger(PassengerRequest passengerRequest);

    PassengerResponse updatePassenger(UUID passengerId, PassengerRequest passengerRequest);

    UUID addPhoto(MultipartFile photoFile, UUID passengerId) throws IOException;

    void deletePassenger(UUID passengerId);

    void updatePassengerRating(RateResponse rateResponse);

}
