//package org.modsen.client;
//
//import org.modsen.dto.response.RateResponse;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.UUID;
//
//@FeignClient(name = "rating-service", url = "${spring.integration.rating-url}")
//public interface RatingClient {
//
//    @GetMapping(path = "/{participantId}")
//    RateResponse getRating(@PathVariable UUID participantId);
//
//}
