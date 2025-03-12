package org.modsen.service;

import java.io.InputStream;

public interface StorageService {

    String uploadFile(String objectName, InputStream inputStream, String contentType);

}
