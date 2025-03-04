package org.modsen.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Dto representing a rating for a ride, including the associated comment and data")
public record RatingResponse(

    @Schema(description = "Rating id", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID ratingId,

    @Schema(
        description = "Id one of the ride participants, who leaves the rate",
        example = "123e4567-e89b-12d3-a456-426614174001"
    )
    UUID fromId,

    @Schema(
        description = "Id one of the ride participants, who is being rated",
        example = "123e4567-e89b-12d3-a456-426614174001"
    )
    UUID toId,

    @Schema(description = "Id of the ride for the rating", example = "123e4567-e89b-12d3-a456-426614174003")
    UUID rideId,

    @Schema(description = "Rating of the ride", example = "4.5")
    Float rating,

    @Schema(description = "Comment about the ride", example = "Great ride!")
    String comment,

    @Schema(description = "Date and time when the rating was created", example = "2023-02-05T14:30:00")
    LocalDateTime createdAt,

    @Schema(description = "Date and time when the rating was last updated", example = "2023-02-06T10:00:00")
    LocalDateTime updatedAt

) {
}
