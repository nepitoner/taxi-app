package org.modsen.service;

import org.modsen.exception.RequestTimeoutException;

import java.io.InputStream;

public interface FileService {

    String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType)
            throws RequestTimeoutException;

}
