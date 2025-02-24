package org.modsen.dto.request;

import static org.modsen.util.constant.PassengerServiceConstant.PHONE_NUMBER_REGEX;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Builder;
import org.modsen.util.SexTypeConverter;

@Builder
@Schema(description = "Dto for getting passenger's information")
public record PassengerRequest(

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

    @Convert(converter = SexTypeConverter.class)
    @NotNull(message = "{incorrect.message}")
    @Schema(description = "Passenger's sex", example = "FEMALE")
    String sex

) {
}
