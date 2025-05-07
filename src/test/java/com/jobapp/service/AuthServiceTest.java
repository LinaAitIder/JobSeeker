package com.jobapp.service;

import com.jobapp.dto.request.LoginRequest;

import com.jobapp.dto.response.AuthResponse;
import com.jobapp.dto.exception.AuthenticationException;
import com.jobapp.model.Candidat;
import com.jobapp.model.Recruteur;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.RecruteurRepository;
import com.jobapp.service.impl.AuthServiceImpl;
import com.jobapp.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    @Mock(lenient = true)
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private CandidatRepository candidatRepository;
    private RecruteurRepository recruteurRepository;
    private PasswordEncoder passwordEncoder;
    private EntrepriseService entrepriseService;
    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        // Configuration du mock JwtUtil
        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("mock-token");

        when(jwtUtil.validateToken(anyString()))
                .thenReturn(true);
        candidatRepository = mock(CandidatRepository.class);
        recruteurRepository = mock(RecruteurRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        entrepriseService = mock(EntrepriseService.class);
        fileStorageService = mock(FileStorageService.class);
        jwtUtil = mock(JwtUtil.class);

        authService = new AuthServiceImpl(
                candidatRepository,
                recruteurRepository,
                passwordEncoder,
                entrepriseService,
                fileStorageService,
                jwtUtil
        );
    }


    @Test
    void authenticate_CandidatSuccess() throws AuthenticationException {
        // 1. Préparation
        LoginRequest request = new LoginRequest();
        request.setEmail("candidat@test.com");
        request.setMotDePasse("password");

        Candidat mockCandidat = new Candidat();
        mockCandidat.setEmail("candidat@test.com");
        mockCandidat.setMotDePasse("encodedPassword");

        // 2. Configuration des mocks
        when(candidatRepository.findByEmail("candidat@test.com"))
                .thenReturn(Optional.of(mockCandidat));
        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("mockToken");

        // 3. Exécution
        AuthResponse response = authService.authenticate(request);

        // 4. Vérifications
        assertNotNull(response);
        assertEquals("mockToken", response.getToken());

        // Vérifie que le repository a bien été appelé
        verify(candidatRepository, times(1)).findByEmail("candidat@test.com");
        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
    }

    @Test
    void authenticate_RecruteurSuccess() throws AuthenticationException {
        // 1. Préparation
        LoginRequest request = new LoginRequest();
        request.setEmail("recruteur@test.com");
        request.setMotDePasse("password");

        Recruteur mockRecruteur = new Recruteur();
        mockRecruteur.setEmail("recruteur@test.com");
        mockRecruteur.setMotDePasse("encodedPassword");

        // 2. Configuration des mocks
        when(recruteurRepository.findByEmail("recruteur@test.com"))
                .thenReturn(Optional.of(mockRecruteur));
        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("mockToken");

        // 3. Exécution
        AuthResponse response = authService.authenticate(request);

        // 4. Vérifications
        assertNotNull(response);
        assertEquals("mockToken", response.getToken());

        verify(recruteurRepository, times(1)).findByEmail("recruteur@test.com");
    }

    @Test
    void authenticate_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@test.com");
        request.setMotDePasse("password");

        when(candidatRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
        when(recruteurRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> {
            authService.authenticate(request);
        });
    }

    @Test
    void authenticate_WrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail("candidat@test.com");
        request.setMotDePasse("wrongpassword");

        Candidat mockCandidat = new Candidat();
        mockCandidat.setEmail("candidat@test.com");
        mockCandidat.setMotDePasse("encodedPassword");

        when(candidatRepository.findByEmail("candidat@test.com"))
                .thenReturn(Optional.of(mockCandidat));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(AuthenticationException.class, () -> {
            authService.authenticate(request);
        });
    }
}
