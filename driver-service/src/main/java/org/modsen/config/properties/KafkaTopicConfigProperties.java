package org.modsen.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaTopicConfigProperties(

    String bootstrapServers,

    RatingConsumer ratingConsumer,

    RideConsumer rideConsumer

) {

    @Getter
    @Setter
    public static class RatingConsumer {
        private String ratingGroupId;
    }

    @Getter
    @Setter
    public static class RideConsumer {
        private String rideGroupId;
    }

}

