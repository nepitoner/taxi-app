package org.example.mapper;

import org.example.dto.PagedResponse;
import org.example.dto.car.CarRequest;
import org.example.dto.car.CarResponse;
import org.example.entity.Car;
import org.example.entity.Driver;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true),
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CarMapper {

    Car mapDtoToEntity(CarRequest carRequest);

    @Mapping(target = "car.isDeleted", defaultValue = "false")
    Car mapDtoToEntity(CarRequest carRequest, UUID id);

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
                .filter(driver -> !driver.getIsDeleted())
                .map(Driver::getId)
                .collect(Collectors.toSet());
    }
}
