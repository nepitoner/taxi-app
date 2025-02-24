package org.modsen.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.response.RateResponse;
import org.modsen.repository.PassengerRepository;
import org.modsen.service.PassengerService;
import org.modsen.service.RedisEventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Listener {

    private final PassengerService passengerService;

    private final PassengerRepository passengerRepository;

    private final RedisEventService redisEventService;

    @KafkaListener(topics = "rating-passenger-driver-topic", groupId = "passenger-driver")
    public void onMessage(RateResponse rateResponse) {

        if (redisEventService.existsByEventId(rateResponse.eventId())) {
            log.info("Passenger Listener. Event with id {} was already processed", rateResponse.eventId());
            return;
        }

        if (passengerRepository.existsByPassengerIdAndIsDeletedIsFalse(UUID.fromString(rateResponse.toId()))) {
            log.info("Passenger Listener. Information about rating being updated {} successfully obtained",
                rateResponse.toId());
            passengerService.updatePassengerRating(rateResponse);
            redisEventService.addEventId(rateResponse.eventId());
        }

    }

}
