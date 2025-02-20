package org.modsen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.config.properties.KafkaTopicConfigProperties;
import org.modsen.dto.request.RideAvailableEvent;
import org.modsen.service.KafkaMessagingService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaTopicConfigProperties.class)
public class KafkaMessagingServiceImpl implements KafkaMessagingService {

    private final KafkaTopicConfigProperties properties;

    private final KafkaTemplate<String, RideAvailableEvent> kafkaTemplate;

    @Override
    public void sendMessage(RideAvailableEvent rideAvailableEvent) {
        kafkaTemplate.send(
            properties.topic()
                .getDriverTopic(),
            properties.groupId(),
            rideAvailableEvent
        );
        log.info("Kafka Message Service. Message {} was successfully sent", rideAvailableEvent);
    }

}
