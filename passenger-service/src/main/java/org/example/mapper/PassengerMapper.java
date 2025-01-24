package org.example.mapper;

import org.example.dto.PassengerDtoRequest;
import org.example.dto.PassengerDtoResponse;
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
    Passenger mapDtoToEntity(PassengerDtoRequest passengerDtoRequest);

    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "profilePictureRef", ignore = true)
    Passenger mapDtoToEntity(PassengerDtoRequest passengerDtoRequest, UUID passengerId,
                             LocalDateTime createdAt, LocalDateTime updatedAt);

    @Mapping(target = "totalPageAmount", ignore = true)
    PassengerDtoResponse mapEntityToDto(Passenger passenger);

    PassengerDtoResponse mapEntityToDto(Passenger passenger, int totalPageAmount);

}
