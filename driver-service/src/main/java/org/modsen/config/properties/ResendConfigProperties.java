package org.modsen.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resend")
public record ResendConfigProperties(

    String url,

    String from,

    String apiKey

) {
}
