package org.modsen.client;

import org.modsen.dto.response.RideResponse;
import org.modsen.exception.MessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ride-service", url = "${spring.integration.ride-url}",
        configuration = {MessageErrorDecoder.class})
public interface RideServiceClient {

    @GetMapping("/{rideId}")
    RideResponse getRideById(@PathVariable UUID rideId);

}
