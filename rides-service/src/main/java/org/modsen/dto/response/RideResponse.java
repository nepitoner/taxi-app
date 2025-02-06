package org.modsen.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.modsen.entity.RideStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "Dto for getting ride's information")
public record RideResponse(

        @NotNull(message = "incorrect.message")
        @Schema(description = "Id of the ride", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
        UUID rideId,

        @NotNull(message = "incorrect.message")
        @Schema(description = "Id of the driver", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
        UUID driverId,

        @NotNull(message = "incorrect.message")
        @Schema(description = "Id of the passenger", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
        UUID passengerId,

        @NotNull(message = "incorrect.message")
        @Schema(description = "Coordinates from where the passenger departs", example = "[-49.279708, -25.46005]")
        List<Double> startingCoordinates,

        @NotNull(message = "incorrect.message")
        @Schema(description = "Coordinates from where the passenger departs", example = "[-49.279708, -25.46005]")
        List<Double> endingCoordinates,

        @NotNull(message = "incorrect.message")
        @Schema(description = "Status of the ride", example = "CREATED")
        RideStatus rideStatus,

        @NotNull(message = "incorrect.message")
        @Schema(description = "Date and time of the order", example = "2025-01-30T15:30:00")
        LocalDateTime orderDateTime,

        @NotNull(message = "incorrect.message")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @Schema(description = "Price of the ride", example = "25.50")
        BigDecimal price,

        @NotNull(message = "incorrect.message")
        @Schema(
                description = "Creation date and time of the ride",
                example = "2025-01-30T15:30:00"
        )
        LocalDateTime createdAt,

        @NotNull(message = "incorrect.message")
        @Schema(
                description = "Update date and time of the ride",
                example = "2025-01-30T15:30:00"
        )
        LocalDateTime updatedAt

) {
}
