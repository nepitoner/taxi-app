package org.modsen.service;

import java.io.IOException;
import java.util.UUID;
import org.modsen.exception.RequestTimeoutException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    UUID saveFileReference(MultipartFile photoFile, UUID driverId) throws IOException, RequestTimeoutException;

}
