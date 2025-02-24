package org.modsen.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka.producer")
public record KafkaTopicConfigProperties(

    Topic topic,

    Group group

) {

    @Getter
    @Setter
    public static class Topic {

        private String passengerDriverTopic;

    }

    @Getter
    @Setter
    public static class Group {

        private String passengerGroup;

        private String driverGroup;

    }

}

