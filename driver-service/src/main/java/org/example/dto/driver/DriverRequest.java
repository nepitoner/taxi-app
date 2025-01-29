package org.example.dto.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.example.utils.SexTypeConverter;

import java.time.LocalDate;

import static org.example.utils.constant.DriverServiceConstant.PHONE_NUMBER_REGEX;

@Builder
@Schema(description = "Dto for getting driver's information")
public record DriverRequest(

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

        @Convert(converter= SexTypeConverter.class)
        @NotNull(message = "incorrect.message")
        @Schema(description = "driver.sex", example = "driver.sex.example")
        Integer sex
) {
}
