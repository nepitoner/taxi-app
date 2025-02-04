package org.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.example.entity.RideStatus;
import org.example.validator.ValidEnum;

@Builder
@Schema(description = "Dto for getting ride's status")
public record RideStatusRequest(

        @NotNull(message = "incorrect.message")
        @ValidEnum(enumClass = RideStatus.class)
        @Schema(description = "Status of the ride", example = "ACCEPTED")
        String rideStatus

) {
}
