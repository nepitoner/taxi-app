package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;

import static org.example.utils.constant.PassengerServiceConstant.INCORRECT_DATA_MESSAGE;
import static org.example.utils.constant.PassengerServiceConstant.PHONE_NUMBER_REGEX;

@Builder
@Schema(description = "Dto for getting passenger's information")
public record PassengerDtoRequest(

        @NotBlank(message = INCORRECT_DATA_MESSAGE)
        @Schema(description = "Passenger's first name", example = "Ivan")
        String firstName,

        @NotBlank(message = INCORRECT_DATA_MESSAGE)
        @Schema(description = "Passenger's last name", example = "Ivanov")
        String lastName,

        @NotNull(message = INCORRECT_DATA_MESSAGE)
        @PastOrPresent(message = INCORRECT_DATA_MESSAGE)
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
                example = "+375(29)1234567"
        )
        @Pattern(
                regexp = PHONE_NUMBER_REGEX,
                message = INCORRECT_DATA_MESSAGE
        )
        String phoneNumber
) {
}
