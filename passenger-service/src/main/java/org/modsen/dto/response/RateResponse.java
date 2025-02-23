package org.modsen.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RateResponse(

        String eventId,

        String  toId,

        float rating

) {
}
