package com.jobapp.service;

import com.jobapp.config.FileStorageProperties;
import com.jobapp.dto.exception.FileStorageException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class FileStorageServiceTest {
    private FileStorageService fileStorageService;
    private Path tempDir;
    private FileStorageProperties props;

    @BeforeEach
    void setUp() {
        tempDir = Path.of("test-uploads");
        props = new FileStorageProperties();
        props.setUploadDir(tempDir.toString());
        fileStorageService = new FileStorageService(props);

        try {
            Files.createDirectories(tempDir);
        } catch (IOException e) {
            fail("Could not create test directory", e);
        }
    }

    @Test
    void storeFile_ShouldSaveFileAndReturnPath() throws Exception {
        MultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "content".getBytes());

        String path = fileStorageService.storeFile(file, "docs", 1L);

        assertTrue(path.startsWith("/docs/1/"));
        assertTrue(Files.exists(tempDir.resolve(path.substring(1))));
    }

    @Test
    void storeProfilePhoto_ShouldUseCorrectFolder() throws Exception {
        MultipartFile photo = new MockMultipartFile(
                "photo", "profile.jpg", "image/jpeg", new byte[10]);

        String path = fileStorageService.storeProfilePhoto(photo, "candidat", 123L);

        assertTrue(path.startsWith("/profile-photos/candidat/123/"));
        assertTrue(Files.exists(tempDir.resolve(path.substring(1))));
    }

    @Test
    void deleteFile_ShouldRemoveFile() throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "content".getBytes());

        fileStorageService.deleteFile("/test.txt");
        assertFalse(Files.exists(testFile));
    }

    @Test
    void deleteFile_WhenNotExists_ShouldNotThrow() {
        assertDoesNotThrow(() -> fileStorageService.deleteFile("/nonexistent.txt"));
    }

    @Test
    void storeFile_WithInvalidName_ShouldThrow() {
        MultipartFile file = new MockMultipartFile(
                "file", "../invalid.txt", "text/plain", "bad".getBytes());

        assertThrows(FileStorageException.class, () ->
                fileStorageService.storeFile(file, "test", 1L));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}