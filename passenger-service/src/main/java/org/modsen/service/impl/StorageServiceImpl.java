package org.modsen.service.impl;

import static org.modsen.util.constant.PassengerServiceConstant.PHOTO_PASSENGER_NAME;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modsen.config.MinioConfigProperties;
import org.modsen.exception.RequestTimeoutException;
import org.modsen.service.PassengerService;
import org.modsen.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final MinioConfigProperties properties;

    private final PassengerService passengerService;

    private final FileService fileService;

    @Override
    public UUID saveFileReference(MultipartFile photoFile, UUID id) throws IOException, RequestTimeoutException {
        String fileRef = fileService.uploadFile(properties.bucket().getPhotoBucketName(),
            PHOTO_PASSENGER_NAME.formatted(id), photoFile.getInputStream(),
            photoFile.getContentType());
        return passengerService.addPhoto(id, fileRef);
    }

}
