package org.modsen.dto.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.modsen.util.SexTypeConverter;

@Builder
@Schema(description = "Dto for getting driver's information")
public record DriverResponse(

    @Schema(description = "Driver's id", example = "71f5135e-fb46-415c-b4cf-bbb9be5692d")
    UUID id,

    @Schema(description = "Driver's first name", example = "Ivan")
    String firstName,

    @Schema(description = "Driver's last name", example = "Ivanov")
    String lastName,

    @Schema(description = "Driver's date of birth", example = "01-01-2000")
    LocalDate dateOfBirth,

    @Schema(description = "Driver's email", example = "name@mail.com")
    String email,

    @Schema(description = "Driver's phone number +375()......., 7 digits with code", example = "+375(29)1234567")
    String phoneNumber,

    @Schema(description = "Indicates whether the driver record is deleted", example = "false")
    boolean isDeleted,

    @Schema(description = "Indicates whether the driver is available", example = "true")
    boolean isAvailable,

    @Convert(converter = SexTypeConverter.class)
    @Schema(description = "Driver's sex", example = "FEMALE")
    String sex,

    @Schema(description = "Driver's rating", example = "4.5")
    float rating,

    @Schema(description = "Driver's created at timestamp", example = "2025-01-23T10:15:30")
    LocalDateTime createdAt,

    @Schema(description = "Driver's updated at timestamp", example = "2025-01-23T10:15:30")
    LocalDateTime updatedAt,

    @Schema(description = "Ids of the driver's cars")
    Set<UUID> carsIds

) {
}
