package org.modsen.config;

import java.util.Arrays;
import org.modsen.utils.converter.IntegerToRideStatusConverter;
import org.modsen.utils.converter.RideStatusToIntegerConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

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
