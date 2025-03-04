package org.modsen.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Dto for getting ride's information for rating service")
public record ShortRideResponse(

    @Schema(description = "Id of the ride", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
    UUID rideId,

    @Schema(description = "Id of the driver", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
    UUID driverId,

    @Schema(description = "Id of the passenger", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
    UUID passengerId

) {
}
