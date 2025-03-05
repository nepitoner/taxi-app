package org.modsen.service.impl;

import static org.modsen.util.constant.DriverServiceConstant.DRIVER_PHOTO_NAME;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modsen.config.properties.MinioConfigProperties;
import org.modsen.exception.RequestTimeoutException;
import org.modsen.service.DriverService;
import org.modsen.service.FileService;
import org.modsen.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final MinioConfigProperties properties;

    private final DriverService driverService;

    private final FileService fileService;

    @Override
    public UUID saveFileReference(MultipartFile photoFile, UUID id) throws IOException, RequestTimeoutException {
        String fileRef = fileService.uploadFile(properties.bucket().getPhotoBucketName(),
            DRIVER_PHOTO_NAME.formatted(id), photoFile.getInputStream(),
            photoFile.getContentType());
        return driverService.addPhoto(id, fileRef);
    }

}
