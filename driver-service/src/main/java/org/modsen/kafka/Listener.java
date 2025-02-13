package org.modsen.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.RateResponse;
import org.modsen.repository.DriverRepository;
import org.modsen.service.DriverService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Listener {

    private final DriverService driverService;

    private final DriverRepository driverRepository;

    @KafkaListener(topics = "rating-passenger-driver-topic", groupId = "passenger-driver")
    public void onMessage(RateResponse rateResponse) {

        if (driverRepository.existsByIdAndIsDeletedIsFalse(UUID.fromString(rateResponse.toId()))) {
            log.info("Information about rating being updated {} successfully obtained", rateResponse.toId());
            driverService.updateDriverRating(rateResponse);
        }

    }

}
