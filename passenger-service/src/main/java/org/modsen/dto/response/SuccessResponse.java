package org.modsen.dto.response;

import lombok.Builder;

@Builder
public record SuccessResponse(

        String passengerId

) {
}
