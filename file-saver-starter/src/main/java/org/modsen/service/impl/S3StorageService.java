package org.modsen.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.modsen.config.MinioConfigProperties;
import org.modsen.service.StorageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    private final MinioClient minioClient;

    private final MinioConfigProperties properties;

    @Override
    public String uploadFile(String objectName, InputStream inputStream, String contentType) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(properties.bucket().getPhotoBucketName())
                .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(properties.bucket().getPhotoBucketName())
                    .build());
            }

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(properties.bucket().getPhotoBucketName())
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build()
            );
            return objectName;

        } catch (ErrorResponseException | InternalException | InvalidKeyException | IOException |
                 NoSuchAlgorithmException | ServerException | XmlParserException | InvalidResponseException |
                 InsufficientDataException e) {
            throw new RuntimeException(e);
        }
    }

}
