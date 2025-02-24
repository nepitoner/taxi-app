package org.modsen.mapper;

import java.util.List;
import java.util.UUID;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.modsen.dto.request.RatingRequest;
import org.modsen.dto.request.RideCommentRequest;
import org.modsen.dto.response.PagedRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.dto.response.RatingResponse;
import org.modsen.entity.Rating;
import org.springframework.data.domain.Page;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface RatingMapper {

    Rating mapRequestToEntity(RatingRequest ratingRequest, UUID fromId, UUID toId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Rating mapDtoToEntityCommentUpdate(RideCommentRequest rideCommentRequest, @MappingTarget Rating rating);

    RatingResponse mapEntityToResponse(Rating rating);

    @Mapping(target = "ratings", source = "responsePage", qualifiedByName = "mapRatings")
    @Mapping(target = "totalRatings", source = "responsePage.totalElements")
    PagedRatingResponse mapPageEntityToPagedDto(int page, int limit, Page<Rating> responsePage);

    RateResponse mapToRateResponse(UUID toId, float rating);

    @Named("mapRatings")
    default List<RatingResponse> mapRatings(Page<Rating> responsePage) {
        return responsePage.get()
            .map(this::mapEntityToResponse)
            .toList();
    }

}
