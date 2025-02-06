package org.modsen.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

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
        UUID passengerId

) {
}
