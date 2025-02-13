package org.modsen.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.dto.response.RateResponse;
import org.modsen.entity.Outbox;
import org.modsen.repository.OutboxRepository;
import org.modsen.service.KafkaMessagingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {

    private final OutboxRepository outboxRepository;

    private final KafkaMessagingService messagingService;

    @Value(value = "${spring.kafka.batch-size}")
    int batchSize;

    @Scheduled(fixedDelay = 60000)
    public void forwardEventsToKafka() {

        List<Outbox> entities = outboxRepository.findAllByOrderByOutboxIdAsc(Pageable.ofSize(batchSize)).toList();
        if (!entities.isEmpty()) {
            for (Outbox entity : entities) {
                messagingService.sendMessage(RateResponse.builder()
                        .toId(entity.getParticipantId().toString())
                        .rating(entity.getRating())
                        .build());
            }
            outboxRepository.deleteAll(entities);
            log.info("Outboxes {} were successfully sent", entities);
        }
    }

}
