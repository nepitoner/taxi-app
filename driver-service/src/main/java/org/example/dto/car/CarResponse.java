package org.example.dto.car;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

import static org.example.utils.constant.DriverServiceConstant.CAR_NUMBER_REGEX;

@Builder
@Schema(description = "Dto for getting information of the car")
public record CarResponse(

        @NotNull(message = "incorrect.message")
        @Schema(description = "car.id", example = "car.id.example")
        UUID id,

        @NotBlank(message = "incorrect.message")
        @Schema(description = "car.color", example = "car.color.example")
        String color,

        @NotBlank(message = "incorrect.message")
        @Schema(description = "car.brand", example = "car.brand.example")
        String brand,

        @NotBlank(message = "incorrect.message")
        @Pattern(
                regexp = CAR_NUMBER_REGEX,
                message = "incorrect.message"
        )
        @Schema(description = "car.number", example = "car.number.example")
        String number,

        @NotNull(message = "incorrect.message")
        @Schema(description = "car.isDeleted", example = "car.isDeleted.example")
        boolean isDeleted,

        @Schema(description = "car.drivers")
        Set<UUID> driversIds
) {
}
