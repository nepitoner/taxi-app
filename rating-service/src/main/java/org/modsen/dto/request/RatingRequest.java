package org.modsen.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Dto for getting rating's information")
public record RatingRequest(

        @NotNull(message = "incorrect.message")
        @Schema(description = "Id of the ride", example = "8e280da2-9e48-4643-ba8f-5c06a6ee848b")
        UUID rideId,

        @NotNull(message = "incorrect.message")
        @DecimalMin(value = "0.0", message = "Rate must be more than 0")
        @DecimalMax(value = "10.0", message = "Rate must be less then 10")
        Float rating,

        @Schema(description = "Optional comment about the ride", example = "It was cool!")
        String rideComment

) {
}
