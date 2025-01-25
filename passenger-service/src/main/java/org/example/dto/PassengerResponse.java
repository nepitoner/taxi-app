package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.example.utils.constant.ExceptionConstant.INCORRECT_DATA_MESSAGE;
import static org.example.utils.constant.PassengerServiceConstant.PHONE_NUMBER_REGEX;

@Builder
@Schema(description = "Dto for getting passenger's information")
public record PassengerResponse(

        @NotNull(message = INCORRECT_DATA_MESSAGE)
        @Schema(description = "Passenger's id", example = "71f5135e-fb46-415c-b4cf-bbb9be5692d")
        UUID passengerId,

        @NotBlank(message = INCORRECT_DATA_MESSAGE)
        @Schema(description = "Passenger's first name", example = "Ivan")
        String firstName,

        @NotBlank(message = INCORRECT_DATA_MESSAGE)
        @Schema(description = "Passenger's last name", example = "Ivanov")
        String lastName,

        @NotNull(message = INCORRECT_DATA_MESSAGE)
        @PastOrPresent(message = "Incorrect date")
        @Schema(description = "Passenger's date of birth", example = "01-01-2000")
        LocalDate dateOfBirth,

        @Email(message = INCORRECT_DATA_MESSAGE)
        @NotBlank(message = INCORRECT_DATA_MESSAGE)
        @Schema(
                description = "Passenger's email", example = "name@mail.com"
        )
        String email,

        @NotBlank(message = INCORRECT_DATA_MESSAGE)
        @Schema(
                description = "Passenger's phone number +375()......., 7 digits with code",
                example = "+375291234567"
        )
        @Pattern(regexp = PHONE_NUMBER_REGEX, message = INCORRECT_DATA_MESSAGE)
        String phoneNumber,

        @NotNull(message = INCORRECT_DATA_MESSAGE)
        @Schema(
                description = "Indicates whether the passenger record is deleted",
                example = "false"
        )
        boolean isDeleted,

        @NotNull(message = INCORRECT_DATA_MESSAGE)
        @Positive(message = "Rating must be a positive number")
        @Schema(
                description = "Passenger's rating, should be a float value greater than 0",
                example = "4.5"
        )
        float rating,

        @NotNull(message = INCORRECT_DATA_MESSAGE)
        @Schema(
                description = "Date and time when the passenger record was created",
                example = "2025-01-23T10:15:30"
        )
        LocalDateTime createdAt,

        @NotNull(message = INCORRECT_DATA_MESSAGE)
        @Schema(
                description = "Date and time when the passenger record was last updated",
                example = "2025-01-23T10:15:30"
        )
        LocalDateTime updatedAt
) {
}
