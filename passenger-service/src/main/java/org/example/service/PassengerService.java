package org.example.service;

import org.example.dto.PassengerDtoRequest;
import org.example.dto.PassengerDtoResponse;

import java.util.List;
import java.util.UUID;

public interface PassengerService {

    UUID registerPassenger(PassengerDtoRequest passengerDtoRequest);

    PassengerDtoResponse updatePassenger(UUID passengerId, PassengerDtoRequest passengerDtoRequest);

    UUID addPhoto(UUID passengerId, String fileRef);

    void deletePassenger(UUID passengerId);

    List<PassengerDtoResponse> getAllPassengers(int page, int limit);

}
