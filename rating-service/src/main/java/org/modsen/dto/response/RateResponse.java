package org.modsen.dto.response;

import lombok.Builder;

@Builder
public record RateResponse(

        String toId,

        float rating

) {
}
