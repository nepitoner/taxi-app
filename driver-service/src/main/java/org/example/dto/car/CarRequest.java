package org.example.dto.car;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static org.example.utils.constant.DriverServiceConstant.CAR_NUMBER_REGEX;

@Schema(description = "Dto for getting information of the car")
public record CarRequest (

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
        String number
) {
}