package org.modsen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "rating")
public record RatingServiceProperties(

        int limit

) {
}
