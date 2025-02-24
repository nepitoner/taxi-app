package org.modsen.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "rating")
public record RatingServiceProperties(

    int limit

) {
}
