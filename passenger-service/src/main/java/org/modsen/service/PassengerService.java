package org.modsen.service;

import org.modsen.dto.request.RequestParams;
import org.modsen.dto.response.PagedPassengerResponse;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.dto.response.RateResponse;

import java.util.UUID;

public interface PassengerService {

    UUID registerPassenger(PassengerRequest passengerRequest);

    PassengerResponse updatePassenger(UUID passengerId, PassengerRequest passengerRequest);

    UUID addPhoto(UUID passengerId, String fileRef);

    void deletePassenger(UUID passengerId);

    PagedPassengerResponse getAllPassengers(RequestParams requestParams);

    void updatePassengerRating(RateResponse rateResponse);

}
