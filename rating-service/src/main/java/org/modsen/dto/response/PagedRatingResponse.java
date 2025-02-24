package org.modsen.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PagedRatingResponse(

    int page,

    int limit,

    long totalRatings,

    List<RatingResponse> ratings

) {
}
