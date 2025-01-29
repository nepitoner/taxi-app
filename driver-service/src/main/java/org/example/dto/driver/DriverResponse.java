package org.example.dto.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.example.utils.SexType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.example.utils.constant.DriverServiceConstant.PHONE_NUMBER_REGEX;

@Builder
@Schema(description = "Dto for getting driver's information")
public record DriverResponse(

        @NotNull(message = "incorrect.message")
        @Schema(description = "driver.id", example = "driver.id.example")
        UUID id,

        @NotBlank(message = "incorrect.message")
        @Schema(description = "driver.name.first", example = "driver.name.first.example")
        String firstName,

        @NotBlank(message = "incorrect.message")
        @Schema(description = "driver.name.last", example = "driver.name.last.example")
        String lastName,

        @NotNull(message = "incorrect.message")
        @PastOrPresent(message = "incorrect.message")
        @Schema(description = "driver.dateOfBirth", example = "driver.dateOfBirth.example")
        LocalDate dateOfBirth,

        @Email(message = "incorrect.message")
        @NotBlank(message = "incorrect.message")
        @Schema(description = "driver.email", example = "driver.email.example")
        String email,

        @NotBlank(message = "incorrect.message")
        @Schema(description = "driver.phoneNumber", example = "driver.phoneNumber.example")
        @Pattern(regexp = PHONE_NUMBER_REGEX, message = "incorrect.message")
        String phoneNumber,

        @NotNull(message = "incorrect.message")
        @Schema(description = "driver.isDeleted", example = "driver.isDeleted.example")
        boolean isDeleted,

        @NotNull(message = "incorrect.message")
        @Schema(description = "driver.sex", example = "driver.sex.example")
        SexType sex,

        @NotNull(message = "incorrect.message")
        @Positive(message = "driver.rating.positive")
        @Schema(
                description = "driver.rating",
                example = "driver.rating.example"
        )
        float rating,

        @NotNull(message = "incorrect.message")
        @Schema(
                description = "driver.createdAt",
                example = "driver.createdAt.example"
        )
        LocalDateTime createdAt,

        @NotNull(message = "incorrect.message")
        @Schema(
                description = "driver.updatedAt",
                example = "driver.updatedAt.example"
        )
        LocalDateTime updatedAt,

        @Schema(description = "driver.cars")
        Set<UUID> carsIds
) {
}
