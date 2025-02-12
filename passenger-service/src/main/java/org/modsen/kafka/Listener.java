package org.modsen.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.service.PassengerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Listener {

    private final PassengerService passengerService;

    @KafkaListener(topics = "rating-passenger-service-topic", groupId = "passenger")
    public void onMessage(UUID id) {
        log.info("Information about rating being updated {} successfully obtained", id);
        passengerService.updatePassengerRating(id);
    }

}
