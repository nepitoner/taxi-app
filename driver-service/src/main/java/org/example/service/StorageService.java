package org.example.service;

import org.example.exception.RequestTimeoutException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface StorageService {

    String uploadFile(String photoBucketName, String s, InputStream inputStream, String contentType) throws RequestTimeoutException;

    UUID saveFileReference(MultipartFile photoFile, UUID driverId) throws IOException, RequestTimeoutException;

}
