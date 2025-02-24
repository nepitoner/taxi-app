package org.modsen.dto.response;

import static org.modsen.util.constant.PassengerServiceConstant.PHONE_NUMBER_REGEX;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.modsen.entity.SexType;
import org.modsen.util.SexTypeConverter;

@Builder
@Schema(description = "Dto for getting passenger's information")
public record PassengerResponse(

    @NotNull(message = "{incorrect.message}")
    @Schema(description = "Passenger's id", example = "71f5135e-fb46-415c-b4cf-bbb9be5692d")
    UUID passengerId,

    @NotBlank(message = "{incorrect.message}")
    @Schema(description = "Passenger's first name", example = "Ivan")
    String firstName,

    @NotBlank(message = "{incorrect.message}")
    @Schema(description = "Passenger's last name", example = "Ivanov")
    String lastName,

    @NotNull(message = "{incorrect.message}")
    @PastOrPresent(message = "{incorrect.message}")
    @Schema(description = "Passenger's date of birth", example = "01-01-2000")
    LocalDate dateOfBirth,

    @Email(message = "{incorrect.message}")
    @NotBlank(message = "{incorrect.message}")
    @Schema(
        description = "Passenger's email", example = "name@mail.com"
    )
    String email,

    @NotBlank(message = "{incorrect.message}")
    @Schema(
        description = "Passenger's phone number +375()......., 7 digits with code",
        example = "+375(29)1234567"
    )
    @Pattern(
        regexp = PHONE_NUMBER_REGEX,
        message = "{incorrect.message}"
    )
    String phoneNumber,

    @NotNull(message = "{incorrect.message}")
    @Schema(
        description = "Indicates whether the passenger record is deleted",
        example = "false"
    )
    boolean isDeleted,

    @NotNull(message = "{incorrect.message}")
    @Positive(message = "{passenger.rating}")
    @Schema(
        description = "Passenger's rating, should be a float value greater than 0",
        example = "4.5"
    )
    float rating,

    @Convert(converter = SexTypeConverter.class)
    @NotNull(message = "{incorrect.message}")
    @Schema(description = "Passenger's sex", example = "FEMALE")
    SexType sex,

    @NotNull(message = "{incorrect.message}")
    @Schema(
        description = "Date and time when the passenger record was created",
        example = "2025-01-23T10:15:30"
    )
    LocalDateTime createdAt,

    @NotNull(message = "{incorrect.message}")
    @Schema(
        description = "Date and time when the passenger record was last updated",
        example = "2025-01-23T10:15:30"
    )
    LocalDateTime updatedAt

) {
}
