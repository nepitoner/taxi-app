package org.modsen.dto;

import lombok.Builder;

@Builder
public record RateResponse(

        String  toId,

        float rating

) {
}
