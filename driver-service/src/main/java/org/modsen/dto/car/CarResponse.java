package org.modsen.dto.car;

import static org.modsen.utils.constant.DriverServiceConstant.CAR_NUMBER_REGEX;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Dto for getting information of the car")
public record CarResponse(

    @NotBlank(message = "{incorrect.message}")
    @Schema(description = "Car's id", example = "71f5135e-fb46-415c-b4cf-bbb9be5692d")
    UUID id,

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
    String number,

    @NotNull(message = "{incorrect.message}")
    @Schema(description = "Indicates whether the car record is deleted",
        example = "false")
    boolean isDeleted,

    @Schema(description = "Ids of the car's drivers")
    Set<UUID> driversIds

) {
}
