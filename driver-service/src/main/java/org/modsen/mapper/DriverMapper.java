package org.modsen.mapper;

import java.time.LocalDateTime;
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
import org.modsen.dto.driver.DriverRequest;
import org.modsen.dto.driver.DriverResponse;
import org.modsen.dto.response.PagedResponse;
import org.modsen.entity.Car;
import org.modsen.entity.Driver;
import org.springframework.data.domain.Page;

@Mapper(uses = {CarMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface DriverMapper {

    Driver mapDtoToEntity(DriverRequest driverRequest);

    Driver mapDtoToEntity(DriverRequest driverRequest, LocalDateTime updatedAt, @MappingTarget Driver driver);

    @Mapping(target = "carsIds", source = "cars", qualifiedByName = "mapCars")
    DriverResponse mapEntityToResponse(Driver driver);

    @Mapping(target = "elements", source = "responsePage", qualifiedByName = "mapDrivers")
    @Mapping(target = "totalAmount", source = "responsePage.totalElements")
    PagedResponse<DriverResponse> mapPageEntityToPagedDto(int page, int limit, Page<Driver> responsePage);

    @Named("mapDrivers")
    default List<DriverResponse> mapDrivers(Page<Driver> responsePage) {
        return responsePage.get()
            .map(this::mapEntityToResponse)
            .toList();
    }

    @Named("mapCars")
    default Set<UUID> mapCars(Set<Car> cars) {
        return cars.stream()
            .map(Car::getId)
            .collect(Collectors.toSet());
    }

}
