package org.modsen.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.modsen.config.properties.KafkaTopicConfigProperties;
import org.modsen.dto.request.RideAvailableEvent;
import org.modsen.dto.response.RateResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaTopicConfigProperties.class)
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, RateResponse> ratingConsumerFactory(KafkaTopicConfigProperties properties) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.bootstrapServers());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, properties.ratingConsumer().getRatingGroupId());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RateResponse.class.getName());
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RateResponse> kafkaRatingListenerContainerFactory(
        KafkaTopicConfigProperties properties
    ) {
        ConcurrentKafkaListenerContainerFactory<String, RateResponse> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ratingConsumerFactory(properties));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, RideAvailableEvent> rideConsumerFactory(KafkaTopicConfigProperties properties) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.bootstrapServers());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, properties.rideConsumer().getRideGroupId());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RideAvailableEvent.class.getName());
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RideAvailableEvent> kafkaRideListenerContainerFactory(
        KafkaTopicConfigProperties properties) {
        ConcurrentKafkaListenerContainerFactory<String, RideAvailableEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(rideConsumerFactory(properties));
        return factory;
    }

}
