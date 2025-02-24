package org.modsen.client;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;
import org.modsen.dto.response.PassengerResponse;
import org.modsen.exception.MessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "passenger-service", url = "${spring.integration.passenger-url}",
    configuration = {MessageErrorDecoder.class})
public interface PassengerClient {

    @GetMapping(value = "/{passengerId}", produces = APPLICATION_JSON_VALUE)
    PassengerResponse getPassengerById(@PathVariable UUID passengerId);

}
