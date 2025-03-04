package org.modsen.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.modsen.entity.SexType;
import org.modsen.util.SexTypeConverter;

@Builder
@Schema(description = "Dto for getting passenger's information")
public record PassengerResponse(

    @Schema(description = "Passenger's id", example = "71f5135e-fb46-415c-b4cf-bbb9be5692d")
    UUID passengerId,

    @Schema(description = "Passenger's first name", example = "Ivan")
    String firstName,

    @Schema(description = "Passenger's last name", example = "Ivanov")
    String lastName,

    @Schema(description = "Passenger's date of birth", example = "01-01-2000")
    LocalDate dateOfBirth,

    @Schema(
        description = "Passenger's email", example = "name@mail.com"
    )
    String email,

    @Schema(
        description = "Passenger's phone number +375()......., 7 digits with code",
        example = "+375(29)1234567"
    )
    String phoneNumber,

    @Schema(
        description = "Indicates whether the passenger record is deleted",
        example = "false"
    )
    boolean isDeleted,

    @Schema(
        description = "Passenger's rating, should be a float value greater than 0",
        example = "4.5"
    )
    float rating,

    @Convert(converter = SexTypeConverter.class)
    @Schema(description = "Passenger's sex", example = "FEMALE")
    SexType sex,

    @Schema(
        description = "Date and time when the passenger record was created",
        example = "2025-01-23T10:15:30"
    )
    LocalDateTime createdAt,

    @Schema(
        description = "Date and time when the passenger record was last updated",
        example = "2025-01-23T10:15:30"
    )
    LocalDateTime updatedAt

) {
}
