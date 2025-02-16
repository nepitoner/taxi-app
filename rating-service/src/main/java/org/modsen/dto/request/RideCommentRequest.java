package org.modsen.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Dto for getting comment about the ride")
public record RideCommentRequest(

        @NotNull(message = "{incorrect.message}")
        @Schema(description = "Author's of comment id", example = "3e5feb5b-b0b7-4587-9ae6-2031f23bef75")
        UUID fromId,

        @NotBlank(message = "{incorrect.message}")
        @Schema(description = "Comment about the ride", example = "I loved it!!")
        String comment

) {
}
