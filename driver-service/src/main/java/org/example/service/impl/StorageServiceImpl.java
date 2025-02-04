package org.example.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.example.config.MinioConfigProperties;
import org.example.exception.RequestTimeoutException;
import org.example.service.DriverService;
import org.example.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.example.utils.constant.DriverServiceConstant.DRIVER_PHOTO_NAME;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final MinioClient minioClient;

    private final MinioConfigProperties properties;

    private final DriverService driverService;

    @Override
    public String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) throws RequestTimeoutException {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
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
                DRIVER_PHOTO_NAME.formatted(id), photoFile.getInputStream(),
                photoFile.getContentType());
        return driverService.addPhoto(id, fileRef);
    }

}
