package org.modsen.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "router")
public record RouterApiProperties(

    String link,

    String apiKey

) {
}