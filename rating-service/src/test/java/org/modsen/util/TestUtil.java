package org.modsen.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.UUID;
import org.modsen.dto.request.RatingRequest;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.request.RideCommentRequest;
import org.modsen.dto.response.RateResponse;
import org.modsen.dto.response.RatingResponse;
import org.modsen.entity.Rating;

public final class TestUtil {

    private TestUtil() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .registerModule(new JavaTimeModule());

    public static String asJson(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static RequestParams requestParams() {
        return RequestParams.builder()
            .page(0)
            .limit(10)
            .sortBy("createdAt")
            .sortDirection("asc")
            .build();
    }

    public static RatingRequest ratingRequest(UUID rideId) {
        return new RatingRequest(rideId, 4.5f, "Great ride!");
    }

    public static RatingResponse ratingResponse(UUID ratingId, UUID fromId, UUID toId, UUID rideId) {
        return new RatingResponse(ratingId, fromId, toId, rideId, 4.5f, "Great ride!",
            LocalDateTime.now(), LocalDateTime.now());
    }

    public static Rating rating(UUID ratingId, UUID fromId, UUID toId, UUID rideId) {
        return Rating.builder()
            .ratingId(ratingId)
            .fromId(fromId)
            .toId(toId)
            .rideId(rideId)
            .rating(4.5f)
            .comment("Great ride!")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public static RateResponse rateResponse(UUID toId) {
        return RateResponse.builder()
            .eventId("1")
            .toId(toId.toString())
            .rating(4.5f)
            .build();
    }

    public static RideCommentRequest rideComment(UUID ratingId) {
        return new RideCommentRequest(ratingId, "Great ride!");
    }

}
