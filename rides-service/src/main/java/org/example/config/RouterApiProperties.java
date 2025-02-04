package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "router")
public record RouterApiProperties(
        String apiKey
) {
}
