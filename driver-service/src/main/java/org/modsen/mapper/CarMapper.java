package org.modsen.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.modsen.dto.car.CarRequest;
import org.modsen.dto.car.CarResponse;
import org.modsen.dto.response.PagedResponse;
import org.modsen.entity.Car;
import org.modsen.entity.Driver;
import org.springframework.data.domain.Page;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CarMapper {

    Car mapDtoToEntity(CarRequest carRequest);

    Car mapDtoToEntity(CarRequest carRequest, @MappingTarget Car car);

    @Mapping(target = "driversIds", source = "drivers", qualifiedByName = "mapDrivers")
    CarResponse mapEntityToResponse(Car car);

    @Mapping(target = "drivers", ignore = true)
    Car mapResponseToEntity(CarResponse carResponse);

    @Mapping(target = "elements", source = "responsePage", qualifiedByName = "mapCars")
    @Mapping(target = "totalAmount", source = "responsePage.totalElements")
    PagedResponse<CarResponse> mapPageEntityToPagedDto(int page, int limit, Page<Car> responsePage);

    @Named("mapCars")
    default List<CarResponse> mapCars(Page<Car> responsePage) {
        return responsePage.get()
            .map(this::mapEntityToResponse)
            .toList();
    }

    @Named("mapDrivers")
    default Set<UUID> mapDrivers(Set<Driver> drivers) {
        return drivers.stream()
            .map(Driver::getId)
            .collect(Collectors.toSet());
    }
}
