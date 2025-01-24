package org.example.mapper;

import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;
import org.example.entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "passengerId", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profilePictureRef", ignore = true)
    Passenger mapDtoToEntity(PassengerRequest passengerRequest);

    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "profilePictureRef", ignore = true)
    Passenger mapDtoToEntity(PassengerRequest passengerRequest, UUID passengerId,
                             LocalDateTime createdAt, LocalDateTime updatedAt);

    @Mapping(target = "totalPageAmount", ignore = true)
    PassengerResponse mapEntityToDto(Passenger passenger);

    PassengerResponse mapEntityToDto(Passenger passenger, int totalPageAmount);

}
