package org.modsen.dto.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.modsen.utils.SexTypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.modsen.utils.constant.DriverServiceConstant.PHONE_NUMBER_REGEX;

@Builder
@Schema(description = "Dto for getting driver's information")
public record DriverResponse(

        @NotNull(message = "{incorrect.message}")
        @Schema(description = "Driver's id", example = "71f5135e-fb46-415c-b4cf-bbb9be5692d")
        UUID id,

        @NotBlank(message = "{incorrect.message}")
        @Schema(description = "Driver's first name", example = "Ivan")
        String firstName,

        @NotBlank(message = "{incorrect.message}")
        @Schema(description = "Driver's last name", example = "Ivanov")
        String lastName,

        @NotNull(message = "{incorrect.message}")
        @PastOrPresent(message = "{incorrect.message}")
        @Schema(description = "Driver's date of birth", example = "01-01-2000")
        LocalDate dateOfBirth,

        @Email(message = "{incorrect.message}")
        @NotBlank(message = "{incorrect.message}")
        @Schema(description = "Driver's email", example = "name@mail.com")
        String email,

        @Schema(description = "Driver's phone number +375()......., 7 digits with code", example = "+375(29)1234567")
        @Pattern(regexp = PHONE_NUMBER_REGEX, message = "{incorrect.message}")
        String phoneNumber,

        @NotNull(message = "{incorrect.message}")
        @Schema(description = "Indicates whether the driver record is deleted", example = "false")
        boolean isDeleted,

        @NotNull(message = "{incorrect.message}")
        @Schema(description = "Indicates whether the driver is available", example = "true")
        boolean isAvailable,

        @Convert(converter = SexTypeConverter.class)
        @NotNull(message = "{incorrect.message}")
        @Schema(description = "Driver's sex", example = "FEMALE")
        String sex,

        @NotNull(message = "{incorrect.message}")
        @Positive(message = "{driver.rating.positive}")
        @Schema(description = "Driver's rating", example = "4.5")
        float rating,

        @NotNull(message = "{incorrect.message}")
        @Schema(description = "Driver's created at timestamp", example = "2025-01-23T10:15:30")
        LocalDateTime createdAt,

        @NotNull(message = "{incorrect.message}")
        @Schema(description = "Driver's updated at timestamp", example = "2025-01-23T10:15:30")
        LocalDateTime updatedAt,

        @Schema(description = "Ids of the driver's cars")
        Set<UUID> carsIds
) {
}
