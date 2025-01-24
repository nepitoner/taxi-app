package org.example.service;

import org.example.exception.RequestTimeoutException;

import java.io.InputStream;

public interface StorageService {

    String uploadFile(String photoBucketName, String s, InputStream inputStream, String contentType) throws RequestTimeoutException;

}
