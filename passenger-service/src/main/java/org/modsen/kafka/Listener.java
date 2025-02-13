package org.modsen.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.response.RateResponse;
import org.modsen.repository.PassengerRepository;
import org.modsen.service.PassengerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Listener {

    private final PassengerService passengerService;

    private final PassengerRepository passengerRepository;

    @KafkaListener(topics = "rating-passenger-driver-topic", groupId = "passenger-driver")
    public void onMessage(RateResponse rateResponse) {

        if (passengerRepository.existsByPassengerIdAndIsDeletedIsFalse(UUID.fromString(rateResponse.toId()))) {
            log.info("Information about rating being updated {} successfully obtained", rateResponse.toId());
            passengerService.updatePassengerRating(rateResponse);
        }

    }

}
