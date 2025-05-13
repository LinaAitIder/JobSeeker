package com.jobapp.controller;

import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.service.AuthHelperService;
import com.jobapp.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

import com.jobapp.dto.exception.AuthenticationException;
import com.jobapp.service.CandidatService;
import com.jobapp.service.FileStorageService;
import com.jobapp.dto.exception.DuplicateEmailException;
import com.jobapp.dto.exception.FileStorageException;
import com.jobapp.dto.request.*;
import com.jobapp.dto.response.*;
import com.jobapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final CandidatService candidatService;
    private final FileStorageService fileStorageService;
    private final AuthHelperService authHelperService;
    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    public AuthController(AuthService authService,
                          CandidatService candidatService,
                          FileStorageService fileStorageService,
                          AuthHelperService authHelperService) {
        this.authService = authService;
        this.candidatService = candidatService;
        this.fileStorageService = fileStorageService;
        this.authHelperService = authHelperService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.authenticate(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(
                    new ErrorResponse("AUTH_ERROR", e.getMessage())
            );
        }
    }

    @RequestMapping(value = "/register/candidat", method = RequestMethod.POST)
    public ResponseEntity<?> registerCandidat(@RequestBody CandidatRegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error));
            throw new IllegalArgumentException("Invalid data");
        }
        System.out.println(request);
        try {
            if (!authService.isPasswordValid(request.getMotDePasse())) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse("VALIDATION_ERROR", "Le mot de passe doit contenir au moins 6 caractères")
                );
            }

            CandidatProfileResponse response = authService.registerCandidat(request);
            return ResponseEntity.status(201).body(response);

        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(409).body(
                    new ErrorResponse("DUPLICATE_EMAIL", e.getMessage())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("SERVER_ERROR", "Erreur lors de l'inscription")
            );
        }
    }
    // Endpoint pour completer le profil (mise a jour)
    @PatchMapping(value = "/candidat/{id}/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> completeCandidatProfile(
            @PathVariable Long id,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String pays,
            @RequestPart(required = false) MultipartFile cv,
            @RequestPart(required = false) List<MultipartFile> certifications,
            @RequestPart(required = false) MultipartFile photoprofil
    ) {
        try {
            // 1. Sauvegarder les fichiers et récupérer leurs chemins
            String cvPath = (cv != null && !cv.isEmpty()) ?
                    fileStorageService.storeFile(cv, "cvs", id) : null;

            List<String> certificationsPaths = (certifications != null) ?
                    certifications.stream()
                            .filter(file -> !file.isEmpty())
                            .map(file -> fileStorageService.storeFile(file, "certifications", id))
                            .collect(Collectors.toList()) : null;
            String photoProfilPath = (photoprofil != null && !photoprofil.isEmpty()) ?
                    fileStorageService.storeFile(photoprofil, "profile-photos", id) : null;

            // 2. Mettre à jour le profil
            CandidatProfileResponse updatedProfile = authService.updateCandidatProfile(
                    id,
                    nom,
                    prenom,
                    telephone,
                    ville,
                    pays,
                    cvPath,
                    certificationsPaths,
                    photoProfilPath
            );

            return ResponseEntity.ok(updatedProfile);

        } catch (FileStorageException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("FILE_ERROR", "Erreur lors de l'enregistrement des fichiers: " + e.getMessage())
            );
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(
                    new ErrorResponse("NOT_FOUND", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("SERVER_ERROR", "Erreur lors de la mise à jour du profil: " + e.getMessage())
            );
        }
    }
    // Endpoint pour récupérer le profil complet
    @RequestMapping(value = "/candidat/{id}/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getCandidateProfile(@PathVariable Long id) {
        try {
            CandidatProfileResponse profile = candidatService.getCandidatProfile(id).getBody();
            return ResponseEntity.ok(profile);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(
                    new ErrorResponse("NOT_FOUND", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("SERVER_ERROR", "Erreur lors de la récupération du profil")
            );
        }
    }

    @RequestMapping(value = "/register/recruteur", method = RequestMethod.POST)
    public ResponseEntity<?> registerRecruteur(@RequestBody RecruteurRegisterRequest request) {
        try {
            if (!authService.isPasswordValid(request.getMotDePasse())) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse("VALIDATION_ERROR", "Le mot de passe doit contenir au moins 6 caractères")
                );
            }

            RecruteurProfileResponse response = authService.registerRecruteur(request);
            return ResponseEntity.status(201).body(response);

        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(409).body(
                    new ErrorResponse("DUPLICATE_EMAIL", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("SERVER_ERROR", "Erreur lors de l'inscription")
            );
        }
    }
    @PatchMapping(value = "/recruteur/{id}/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> completeRecruteurProfile(
            @PathVariable Long id,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String entrepriseNom,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String telephone,
            @RequestPart(required = false) MultipartFile photoprofil) {
        try {
            String photoProfilPath = (photoprofil != null && !photoprofil.isEmpty()) ?
                    fileStorageService.storeProfilePhoto(photoprofil, "recruteur", id) : null;

            RecruteurProfileResponse updatedProfile = authService.updateRecruteurProfile(
                    id,
                    nom,
                    prenom,
                    entrepriseNom,
                    position,
                    telephone,
                    photoProfilPath
            );

            return ResponseEntity.ok(updatedProfile);

        } catch (FileStorageException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("FILE_ERROR", "Erreur lors de l'enregistrement de la photo: " + e.getMessage())
            );
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(
                    new ErrorResponse("NOT_FOUND", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("SERVER_ERROR", "Erreur lors de la mise à jour du profil: " + e.getMessage())
            );
        }
    }

    @GetMapping("/exists/email")
    public ResponseEntity<UserIdentityResponse> checkEmailExists(
            @RequestParam String email
    ) {
        UserIdentityResponse response = authHelperService.checkUserExists(email);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            String newToken = jwtUtil.refreshToken(request.getToken());

            // Extraire les informations necessaires pour AuthResponse
            Claims claims = jwtUtil.extractAllClaims(newToken);
            String role = claims.get("role", String.class);
            Long userId = claims.get("userId", Long.class);

            return ResponseEntity.ok(new AuthResponse(newToken, role, userId));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token trop ancien - veuillez vous reconnecter");
        } catch (MalformedJwtException e) {
            return ResponseEntity.badRequest().body("Token invalide");
}
}
}
