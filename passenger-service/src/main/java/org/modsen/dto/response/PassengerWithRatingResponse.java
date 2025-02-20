package org.modsen.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PassengerWithRatingResponse(

        @Schema(description = "Passenger's id", example = "71f5135e-fb46-415c-b4cf-bbb9be5692d")
        UUID passengerId,

        @Schema(
                description = "Passenger's rating, should be a float value greater than 0",
                example = "4.5"
        )
        float rating

) {
}
