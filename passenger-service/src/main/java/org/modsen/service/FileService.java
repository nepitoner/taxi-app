package org.modsen.service;

import java.io.InputStream;
import org.modsen.exception.RequestTimeoutException;

public interface FileService {

    String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType)
        throws RequestTimeoutException;

}
