package org.modsen.mapper;

import org.modsen.dto.request.RideRequest;
import org.modsen.dto.request.RideStatusRequest;
import org.modsen.dto.response.PagedRideResponse;
import org.modsen.dto.response.RideResponse;
import org.modsen.dto.response.ShortRideResponse;
import org.modsen.entity.Ride;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true),
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface RideMapper {

    @Mapping(target = "rideStatus", constant = "CREATED")
    Ride mapRequestToEntity(RideRequest rideRequest, LocalDateTime orderDateTime, BigDecimal price);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ride mapDtoToEntity(RideRequest rideRequest, @MappingTarget Ride ride);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ride mapDtoToEntityStatusUpdate(RideStatusRequest rideStatusRequest, @MappingTarget Ride ride);

    RideResponse mapEntityToResponse(Ride ride);

    ShortRideResponse mapEntityToShortResponse(Ride ride);

    @Mapping(target = "rides", source = "responsePage", qualifiedByName = "mapRides")
    @Mapping(target = "totalRides", source = "responsePage.totalElements")
    PagedRideResponse mapPageEntityToPagedDto(int page, int limit, Page<Ride> responsePage);

    @Named("mapRides")
    default List<RideResponse> mapRides(Page<Ride> responsePage) {
        return responsePage.get()
                .map(this::mapEntityToResponse)
                .toList();
    }

}
