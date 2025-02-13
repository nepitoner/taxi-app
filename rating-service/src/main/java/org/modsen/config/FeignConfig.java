package org.modsen.config;

import feign.Logger;
import feign.Request.Options;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 4);
    }

    @Bean
    public Options options() {
        return new Options(5, TimeUnit.SECONDS, 10, TimeUnit.SECONDS, true);
    }

}
