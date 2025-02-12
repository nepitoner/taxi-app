package org.modsen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.config.properties.KafkaTopicConfigProperties;
import org.modsen.service.KafkaMessagingService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaTopicConfigProperties.class)
public class KafkaMessagingServiceImpl implements KafkaMessagingService {

    private final KafkaTopicConfigProperties properties;

    private final KafkaTemplate<String, UUID> kafkaTemplate;

    @Override
    public void sendPassengerMessage(UUID message) {
        kafkaTemplate.send(
                properties.topic()
                        .getPassengerTopic(),
                properties.groupId(), message);
        log.info("Message {} was successfully sent to passenger service", message);
    }

}
