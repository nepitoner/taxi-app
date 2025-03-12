package org.modsen.service.impl;

import static org.modsen.util.Constant.PHOTO_NAME;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modsen.service.StorageService;
import org.modsen.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final StorageService storageService;

    @Override
    public String saveFileReference(MultipartFile photoFile, UUID id) throws IOException {
        return storageService.uploadFile(PHOTO_NAME.formatted(id), photoFile.getInputStream(),
            photoFile.getContentType());
    }

}
