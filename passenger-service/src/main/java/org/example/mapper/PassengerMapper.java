package org.example.mapper;

import org.example.dto.PagedPassengerResponse;
import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;
import org.example.entity.Passenger;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true),
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface PassengerMapper {

    Passenger mapDtoToEntity(PassengerRequest passengerRequest);

    Passenger mapDtoToEntity(PassengerRequest passengerRequest, UUID passengerId,
                             LocalDateTime createdAt, LocalDateTime updatedAt);

    PassengerResponse mapEntityToDto(Passenger passenger);

    PagedPassengerResponse mapPageEntityToPagedDto(int page, int limit, long totalPassengers, List<PassengerResponse> passengers);

}
