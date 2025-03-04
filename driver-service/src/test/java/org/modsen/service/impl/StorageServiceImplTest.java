package org.modsen.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.minio.MinioClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.config.properties.MinioConfigProperties;
import org.modsen.exception.RequestTimeoutException;
import org.modsen.service.DriverService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private DriverService driverService;

    @Mock
    private MinioConfigProperties properties;

    @Mock
    private MinioConfigProperties.Bucket bucket;

    @InjectMocks
    private StorageServiceImpl storageService;

    private UUID driverId;
    private String expectedFileRef;
    private String photoBucketName;
    private MultipartFile photoFile;

    @BeforeEach
    void setUp() throws IOException {
        driverId = UUID.randomUUID();
        expectedFileRef = "driver_photo_" + driverId;
        photoBucketName = "photo-bucket";
        photoFile = new MockMultipartFile(expectedFileRef, expectedFileRef,
            MediaType.IMAGE_JPEG_VALUE, new ByteArrayInputStream("photo".getBytes()));
    }

    @Test
    @DisplayName("Test saving file reference")
    void testSaveFileReference() throws IOException, RequestTimeoutException {
        when(properties.bucket()).thenReturn(bucket);
        when(properties.bucket().getPhotoBucketName()).thenReturn(photoBucketName);
        when(driverService.addPhoto(driverId, expectedFileRef)).thenReturn(driverId);

        UUID result = storageService.saveFileReference(photoFile, driverId);

        assertThat(result).isEqualTo(driverId);
        verify(driverService).addPhoto(driverId, expectedFileRef);
    }

    @Test
    @DisplayName("Test saving file reference but throws IOException")
    void testSaveFileReference_IOException() throws IOException {
        MultipartFile photoFile = mock(MultipartFile.class);
        when(properties.bucket()).thenReturn(MinioConfigProperties.Bucket.builder().build());
        when(photoFile.getInputStream()).thenThrow(new IOException("Failed to get input stream"));

        assertThatThrownBy(() -> storageService.saveFileReference(photoFile, driverId))
            .isInstanceOf(IOException.class)
            .hasMessage("Failed to get input stream");

        verify(driverService, never()).addPhoto(any(), any());
    }

    @Test
    @DisplayName("Test saving file reference but throws RequestTimeoutException")
    void testSaveFileReference_RequestTimeoutException() {
        when(properties.bucket()).thenReturn(bucket);
        when(bucket.getPhotoBucketName()).thenReturn(null);

        assertThatThrownBy(() -> storageService.saveFileReference(photoFile, driverId))
            .isInstanceOf(RequestTimeoutException.class)
            .hasMessage("bucket name must not be null.");

        verify(driverService, never()).addPhoto(any(), any());
    }

}