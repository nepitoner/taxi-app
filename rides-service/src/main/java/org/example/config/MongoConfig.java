package org.example.config;

import org.example.utils.converter.IntegerToRideStatusConverter;
import org.example.utils.converter.RideStatusToIntegerConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new RideStatusToIntegerConverter(),
                new IntegerToRideStatusConverter()
        ));
    }

}
