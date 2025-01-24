package org.example.service;

import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;

import java.util.List;
import java.util.UUID;

public interface PassengerService {

    UUID registerPassenger(PassengerRequest passengerRequest);

    PassengerResponse updatePassenger(UUID passengerId, PassengerRequest passengerRequest);

    UUID addPhoto(UUID passengerId, String fileRef);

    void deletePassenger(UUID passengerId);

    List<PassengerResponse> getAllPassengers(int page, int limit);

}
