package org.modsen.mapper;

import org.modsen.dto.response.PagedPassengerResponse;
import org.modsen.dto.request.PassengerRequest;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.dto.response.PassengerWithRatingResponse;
import org.modsen.entity.Passenger;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

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

    PassengerWithRatingResponse mapEntityToPassengerIdWithRating(Passenger passenger);

    @Mapping(target = "passengers", source = "responsePage", qualifiedByName = "mapPassengers")
    @Mapping(target = "totalPassengers", source = "responsePage.totalElements")
    PagedPassengerResponse mapPageEntityToPagedDto(int page, int limit, Page<Passenger> responsePage);

    @Named("mapPassengers")
    default List<PassengerResponse> mapPassengers(Page<Passenger> responsePage) {
        return responsePage.get()
                .map(this::mapEntityToDto)
                .toList();
    }

}
