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

import static org.example.utils.constant.PassengerServiceConstant.PHONE_NUMBER_REGEX;

@Builder
@Schema(description = "Dto for getting passenger's information")
public record PassengerResponse(

        @NotNull(message = "incorrect.message")
        @Schema(description = "passenger.id", example = "passenger.id.example")
        UUID passengerId,

        @NotBlank(message = "incorrect.message")
        @Schema(description = "passenger.name.first", example = "passenger.name.first.example")
        String firstName,

        @NotBlank(message = "incorrect.message")
        @Schema(description = "passenger.name.last", example = "passenger.name.last.example")
        String lastName,

        @NotNull(message = "incorrect.message")
        @PastOrPresent(message = "incorrect.message")
        @Schema(description = "passenger.dateOfBirth", example = "passenger.dateOfBirth.example")
        LocalDate dateOfBirth,

        @Email(message = "incorrect.message")
        @NotBlank(message = "incorrect.message")
        @Schema(
                description = "passenger.email", example = "passenger.email.example"
        )
        String email,

        @NotBlank(message = "incorrect.message")
        @Schema(
                description = "passenger.phoneNumber",
                example = "passenger.phoneNumber.example"
        )
        @Pattern(regexp = PHONE_NUMBER_REGEX, message = "incorrect.message")
        String phoneNumber,

        @NotNull(message = "incorrect.message")
        @Schema(
                description = "passenger.isDeleted",
                example = "passenger.isDeleted.example"
        )
        boolean isDeleted,

        @NotNull(message = "incorrect.message")
        @Positive(message = "Rating must be a positive number")
        @Schema(
                description = "passenger.rating",
                example = "passenger.rating.example"
        )
        float rating,

        @NotNull(message = "incorrect.message")
        @Schema(
                description = "passenger.createdAt",
                example = "passenger.createdAt.example"
        )
        LocalDateTime createdAt,

        @NotNull(message = "incorrect.message")
        @Schema(
                description = "passenger.updatedAt",
                example = "passenger.updatedAt.example"
        )
        LocalDateTime updatedAt
) {
}
