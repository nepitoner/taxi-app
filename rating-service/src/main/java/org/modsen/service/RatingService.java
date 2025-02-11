package org.modsen.service;

import org.modsen.dto.request.RatingRequest;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.request.RideCommentRequest;
import org.modsen.dto.response.PagedRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.dto.response.RatingResponse;
import java.util.UUID;

public interface RatingService {

    PagedRatingResponse getAllRatings(RequestParams requestParams);

    RateResponse getRateById(UUID participantId);

    UUID createRating(RatingRequest request, UUID participantId);

    RatingResponse addRideComment(UUID ratingId, RideCommentRequest request);

}
