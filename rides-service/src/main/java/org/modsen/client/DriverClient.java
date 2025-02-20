package org.modsen.client;

import java.util.UUID;
import org.modsen.exception.MessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "driver-service", url = "${spring.integration.driver-url}",
    configuration = {MessageErrorDecoder.class})
public interface DriverClient {

    @PostMapping(value = "/available/{driverId}")
    void changeDriverAvailableStatus(@PathVariable UUID driverId);

}
