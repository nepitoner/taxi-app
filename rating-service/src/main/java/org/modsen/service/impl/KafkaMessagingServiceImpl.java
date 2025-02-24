package org.modsen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.config.properties.KafkaTopicConfigProperties;
import org.modsen.dto.response.RateResponse;
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

    private final KafkaTemplate<String, RateResponse> kafkaTemplate;

    @Override
    public void sendMessage(RateResponse rateResponse) {
        kafkaTemplate.send(
                properties.topic()
                        .getPassengerDriverTopic(),
                properties.groupId(), rateResponse);
        log.info("Kafka Messaging Service. Message {} was sent", rateResponse);
    }

}
