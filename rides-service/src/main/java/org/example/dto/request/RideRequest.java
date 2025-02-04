package org.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "Dto for getting ride's information")
public record RideRequest(

        @NotNull(message = "incorrect.message")
        @Schema(description = "Id of the driver", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
        UUID driverId,

        @NotNull(message = "incorrect.message")
        @Schema(description = "Id of the passenger", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
        UUID passengerId,


        @NotNull(message = "incorrect.message")
        @Schema(
                description = "List of starting coordinates in the format [latitude, longitude]",
                example = "[40.7128, -74.0060]")
        List<@DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
                        @DecimalMax(value = "90.0",
                                message = "Latitude must be between -90 and 90") Double> startingCoordinates,

        @NotNull(message = "incorrect.message")
        @Schema(description = "List of ending coordinates in the format [latitude, longitude]",
                example = "[34.0522, -118.2437]")
        List<@DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90") Double> endingCoordinates

) {
}
