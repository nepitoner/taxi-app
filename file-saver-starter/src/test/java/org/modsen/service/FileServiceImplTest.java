package org.modsen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modsen.service.impl.FileServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private StorageService s3StorageService;

    @InjectMocks
    private FileServiceImpl fileService;

    private UUID id;
    private String expectedFileRef;
    private MultipartFile photoFile;

    @BeforeEach
    void setUp() throws IOException {
        id = UUID.randomUUID();
        expectedFileRef = "photo_" + id;
        photoFile = new MockMultipartFile(expectedFileRef, expectedFileRef,
            MediaType.IMAGE_JPEG_VALUE, new ByteArrayInputStream("photo".getBytes()));
    }

    @Test
    @DisplayName("Test saving file reference")
    void testSaveFileReference() throws IOException {
        when(s3StorageService.uploadFile(eq(expectedFileRef), any(InputStream.class), eq(photoFile.getContentType())))
            .thenReturn(expectedFileRef);

        String result = fileService.saveFileReference(photoFile, id);

        assertThat(result).isEqualTo(expectedFileRef);
    }

    @Test
    @DisplayName("Test saving file reference but throws IOException")
    void testSaveFileReference_IOException() throws IOException {
        MultipartFile photoFile = mock(MultipartFile.class);
        when(photoFile.getInputStream()).thenThrow(new IOException("Failed to get input stream"));

        assertThatThrownBy(() -> fileService.saveFileReference(photoFile, id))
            .isInstanceOf(IOException.class)
            .hasMessage("Failed to get input stream");
    }

}