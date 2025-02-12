package org.modsen.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka.producer")
public record KafkaTopicConfigProperties(

    Topic topic,

    String groupId

) {

    @Getter
    @Setter
    public static class Topic {
        private String passengerTopic;
    }
}

