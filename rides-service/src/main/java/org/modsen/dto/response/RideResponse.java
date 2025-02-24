package org.modsen.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.modsen.entity.RideStatus;

@Builder
@Schema(description = "Dto for getting ride's information")
public record RideResponse(

    @Schema(description = "Id of the ride", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
    UUID rideId,

    @Schema(description = "Id of the driver", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
    UUID driverId,

    @Schema(description = "Id of the passenger", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
    UUID passengerId,

    @Schema(description = "Coordinates from where the passenger departs", example = "[-49.279708, -25.46005]")
    List<Double> startingCoordinates,

    @Schema(description = "Coordinates from where the passenger departs", example = "[-49.279708, -25.46005]")
    List<Double> endingCoordinates,

    @Schema(description = "Status of the ride", example = "CREATED")
    RideStatus rideStatus,

    @Schema(description = "Date and time of the order", example = "2025-01-30T15:30:00")
    LocalDateTime orderDateTime,

    @Schema(description = "Price of the ride", example = "25.50")
    BigDecimal price,

    @Schema(description = "Creation date and time of the ride", example = "2025-01-30T15:30:00")
    LocalDateTime createdAt,

    @Schema(description = "Update date and time of the ride", example = "2025-01-30T15:30:00")
    LocalDateTime updatedAt

) {
}
