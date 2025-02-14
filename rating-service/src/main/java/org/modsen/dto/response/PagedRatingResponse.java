package org.modsen.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record PagedRatingResponse(

        int page,

        int limit,

        long totalRatings,

        List<RatingResponse> ratings

) {
}
