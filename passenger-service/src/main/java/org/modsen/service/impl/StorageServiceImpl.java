package org.modsen.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.modsen.config.MinioConfigProperties;
import org.modsen.exception.RequestTimeoutException;
import org.modsen.service.PassengerService;
import org.modsen.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final MinioClient minioClient;

    private final MinioConfigProperties properties;

    private final PassengerService passengerService;

    public final static String PHOTO_PASSENGER_NAME = "passenger_photo_%s";

    @Override
    public String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) throws RequestTimeoutException {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs
                        .builder()
                        .bucket(bucketName)
                        .build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build()
            );
            return objectName;
        } catch (Exception e) {
            throw new RequestTimeoutException(e.getMessage());
        }
    }

    @Override
    public UUID saveFileReference(MultipartFile photoFile, UUID id) throws IOException, RequestTimeoutException {
        String fileRef = uploadFile(properties.bucket().getPhotoBucketName(),
                PHOTO_PASSENGER_NAME.formatted(id), photoFile.getInputStream(),
                photoFile.getContentType());
        return passengerService.addPhoto(id, fileRef);
    }

}
