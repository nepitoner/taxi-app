package org.example.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioConfigProperties.class)
public class MinioConfig {
    @Bean
    public MinioClient generateMinioClient(MinioConfigProperties properties) {
        return MinioClient.builder()
                .endpoint(properties.url())
                .credentials(properties.access().getName(), properties.access().getSecret())
                .build();
    }
}
