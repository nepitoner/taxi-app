package org.modsen.service;

import java.io.IOException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String saveFileReference(MultipartFile photoFile, UUID id) throws IOException;

}
