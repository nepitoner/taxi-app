package org.modsen.kafka;

import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.modsen.dto.response.RateResponse;
import org.modsen.entity.RatingChangeEvent;
import org.modsen.repository.RatingChangeEventRepository;
import org.modsen.service.KafkaMessagingService;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingChangedEventProducer {

    private final RatingChangeEventRepository ratingChangeEventRepository;

    private final KafkaMessagingService messagingService;

    private final Clock clock;

    @Scheduled(cron = "* * * * * *")
    @SchedulerLock(name = "RatingChangedEventProducer_forwardEventsToKafka",
        lockAtLeastFor = "PT30S", lockAtMostFor = "PT1M")
    public void forwardEventsToKafka() {

        List<RatingChangeEvent> events = ratingChangeEventRepository
            .findTop100ByChangedAtLessThanEqual(LocalDateTime.now(clock), Pageable.ofSize(100)).toList();

        if (!events.isEmpty()) {
            for (RatingChangeEvent event : events) {
                processEvent(event);
            }
            log.info("Rating Changed Event Producer. {} outboxes were successfully sent", events.size());
        }
    }

    @Transactional
    public void processEvent(RatingChangeEvent ratingChangeEvent) {
        messagingService.sendMessage(RateResponse.builder()
                .eventId(ratingChangeEvent.getEventId())
                .toId(ratingChangeEvent.getParticipantId().toString())
                .rating(ratingChangeEvent.getRating())
            .build());

        log.info("Rating Changed Event Producer. Process event with id {}", ratingChangeEvent.getEventId());
        ratingChangeEventRepository.delete(ratingChangeEvent);
    }

}
