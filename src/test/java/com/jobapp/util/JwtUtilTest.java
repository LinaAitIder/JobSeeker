package com.jobapp.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;
    // Clé Base64 valide (64 caractères pour HMAC-SHA256)
    private static final String TEST_SECRET = "U3VwZXJTZWN1cmVLZXlGb3JKV1RDb25maWd1cmF0aW9uMTIzNDU2Nzg5MDEyMzQ1Njc4OTA=";
    private static final long TEST_EXPIRATION = 86400000; // 24h

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(TEST_SECRET, TEST_EXPIRATION);
    }

    @Test
    void generateToken_ValidInput_ReturnsToken() {
        String token = jwtUtil.generateToken("test@email.com", "CANDIDAT");
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.split("\\.").length == 3); // Vérifie la structure JWT
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        String token = jwtUtil.generateToken("test@email.com", "CANDIDAT");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        assertFalse(jwtUtil.validateToken("token.invalide.abc"));
    }

    @Test
    void extractEmail_ValidToken_ReturnsEmail() {
        String expectedEmail = "test@email.com";
        String token = jwtUtil.generateToken(expectedEmail, "CANDIDAT");
        assertEquals(expectedEmail, jwtUtil.extractEmail(token));
    }

    @Test
    void extractRole_ValidToken_ReturnsRole() {
        String expectedRole = "RECRUTEUR";
        String token = jwtUtil.generateToken("test@email.com", expectedRole);
        assertEquals(expectedRole, jwtUtil.extractRole(token));
    }

    @Test
    void isTokenExpired_NotExpired_ReturnsFalse() {
        String token = jwtUtil.generateToken("test@email.com", "CANDIDAT");
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void constructor_InvalidKey_ThrowsException() {
        assertThrows(RuntimeException.class,
                () -> new JwtUtil("clé-invalide", TEST_EXPIRATION));
    }

    // Nouveau test pour vérifier l'encodage
    @Test
    void tokenEncoding_Decoding_Consistent() {
        String email = "test@email.com";
        String role = "CANDIDAT";
        String token = jwtUtil.generateToken(email, role);

        String decodedEmail = jwtUtil.extractEmail(token);
        String decodedRole = jwtUtil.extractRole(token);

        assertEquals(email, decodedEmail);
        assertEquals(role, decodedRole);
    }
}