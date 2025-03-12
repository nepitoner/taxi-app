package org.modsen.config;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioConfigProperties(String url,
                                    Access access,
                                    Bucket bucket) {

    @Getter
    @Setter
    public static class Access {
        private String name;
        private String secret;
    }

    @Builder
    @Getter(AccessLevel.PUBLIC)
    @Setter
    public static class Bucket {
        private final String photoBucketName;
    }

}
