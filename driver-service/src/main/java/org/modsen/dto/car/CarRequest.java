package org.modsen.dto.car;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static org.modsen.utils.constant.DriverServiceConstant.CAR_NUMBER_REGEX;

@Schema(description = "Dto for getting information of the car")
public record CarRequest (

        @NotBlank(message = "{incorrect.message}")
        @Schema(description = "Color of the car", example = "Red")
        String color,

        @NotBlank(message = "{incorrect.message}")
        @Schema(description = "Brand of the car", example = "Benz")
        String brand,

        @NotBlank(message = "{incorrect.message}")
        @Pattern(
                regexp = CAR_NUMBER_REGEX,
                message = "{incorrect.message}"
        )
        @Schema(description = "Unique number of the car", example = "8471KI-5")
        String number

) {
}