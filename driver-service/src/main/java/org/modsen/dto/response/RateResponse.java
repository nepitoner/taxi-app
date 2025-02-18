package org.modsen.dto.response;

import lombok.Builder;

@Builder
public record RateResponse(

        String eventId,

        String  toId,

        float rating

) {
}
