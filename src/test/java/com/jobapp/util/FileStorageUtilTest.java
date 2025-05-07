package com.jobapp.util;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;

public class FileStorageUtilTest {

    public static MockMultipartFile createPdfFile(String filename) {
        return new MockMultipartFile(
                "file",
                filename,
                "application/pdf",
                "contenu-test".getBytes()
        );
    }

    public static MockMultipartFile createLargePdfFile() throws IOException {
        byte[] largeContent = new byte[6 * 1024 * 1024]; // 6MB (> limite de 5MB)
        return new MockMultipartFile(
                "file",
                "large.pdf",
                "application/pdf",
                largeContent
        );
    }
}