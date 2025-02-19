package org.modsen.client;


import org.modsen.dto.response.PassengerResponse;
import org.modsen.exception.MessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "passenger-service", url = "${spring.integration.passenger-url}",
    configuration = {MessageErrorDecoder.class})
public interface PassengerClient {

    @GetMapping(value = "/{passengerId}", produces = APPLICATION_JSON_VALUE)
    PassengerResponse getPassengerById(@PathVariable UUID passengerId);

}
