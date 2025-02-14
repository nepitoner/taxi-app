package org.modsen.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Dto for getting comment about the ride")
public record RideCommentRequest(

        @NotBlank(message = "incorrect.message")
        @Schema(description = "Comment about the ride", example = "I loved it!!")
        String comment

) {
}
