package org.modsen.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.modsen.config.properties.KafkaTopicConfigProperties;
import org.modsen.dto.response.RateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;


@Configuration
@EnableConfigurationProperties(KafkaTopicConfigProperties.class)
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServers;

    @Bean
    NewTopic newTopic(KafkaTopicConfigProperties properties) {
        return new NewTopic(properties.topic()
                .getPassengerDriverTopic(),
                1,
                (short) 1);
    }

    @Bean
    public KafkaTemplate<String, RateResponse> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public DefaultKafkaProducerFactory<String, RateResponse> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

}
