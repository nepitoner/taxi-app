package org.modsen.kafka;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.response.RateResponse;
import org.modsen.repository.DriverRepository;
import org.modsen.service.DriverService;
import org.modsen.service.RedisEventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingListener {

    private final DriverService driverService;

    private final DriverRepository driverRepository;

    private final RedisEventService redisEventService;

    @KafkaListener(
        topics = "${spring.kafka.rating-consumer.rating-topic}",
        groupId = "${spring.kafka.rating-consumer.rating-group-id}",
        containerFactory = "kafkaRatingListenerContainerFactory"
    )
    public void onMessage(RateResponse rateResponse) {

        if (redisEventService.existsByEventId(rateResponse.eventId())) {
            log.info("Driver Listener. Event with id {} was already processed", rateResponse.eventId());
            return;
        }

        if (driverRepository.existsByIdAndIsDeletedIsFalse(UUID.fromString(rateResponse.toId()))) {
            log.info("Driver Listener. Information about rating being updated {} successfully obtained",
                rateResponse.toId());
            driverService.updateDriverRating(rateResponse);
            redisEventService.addEventId(rateResponse.eventId());
        }

    }

}
