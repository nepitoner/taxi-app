package org.modsen.dto.car;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Dto for getting information of the car")
public record CarResponse(

    @Schema(description = "Car's id", example = "71f5135e-fb46-415c-b4cf-bbb9be5692d")
    UUID id,

    @Schema(description = "Color of the car", example = "Red")
    String color,

    @Schema(description = "Brand of the car", example = "Benz")
    String brand,

    @Schema(description = "Unique number of the car", example = "8471KI-5")
    String number,

    @Schema(description = "Indicates whether the car record is deleted",
        example = "false")
    boolean isDeleted,

    @Schema(description = "Ids of the car's drivers")
    Set<UUID> driversIds

) {
}
