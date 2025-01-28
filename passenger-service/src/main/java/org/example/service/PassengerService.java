package org.example.service;

import org.example.dto.PagedPassengerResponse;
import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;

import java.util.UUID;

public interface PassengerService {

    UUID registerPassenger(PassengerRequest passengerRequest);

    PassengerResponse updatePassenger(UUID passengerId, PassengerRequest passengerRequest);

    UUID addPhoto(UUID passengerId, String fileRef);

    void deletePassenger(UUID passengerId);

    PagedPassengerResponse getAllPassengers(int page, int limit);

}
