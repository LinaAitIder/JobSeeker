package com.jobapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobapp.config.FileStorageProperties;
import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.dto.request.CertificationRequest;
import com.jobapp.dto.request.UpdateCandidatProfileRequest;
import com.jobapp.dto.request.UpdatePasswordRequest;
import com.jobapp.dto.response.*;
import com.jobapp.model.Candidat;
import com.jobapp.model.Certification;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.service.CandidatService;
import com.jobapp.service.FileStorageService;
import com.jobapp.service.CertificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidat")
public class CandidatController {
    private static final Logger logger = LoggerFactory.getLogger(CandidatController.class);
    private final CandidatService candidatService;
    private final FileStorageService fileStorageService;
    private final FileStorageProperties fileStorageProperties;
    private final CandidatRepository candidatRepository;
    private final CertificationService certificationService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CandidatController(CandidatService candidatService,
                              FileStorageService fileStorageService,
                              FileStorageProperties fileStorageProperties,
                              CandidatRepository candidatRepository,
                              CertificationService certificationService,
                              ObjectMapper objectMapper) {
        this.candidatService = candidatService;
        this.fileStorageService = fileStorageService;
        this.fileStorageProperties = fileStorageProperties;
        this.candidatRepository = candidatRepository;
        this.certificationService = certificationService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatProfileResponse> getCandidatProfile(@PathVariable Long id) {
        return candidatService.getCandidatProfile(id);
    }

    @PutMapping(value = "/{id}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCandidatProfile(
            @PathVariable Long id,
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "prenom", required = false) String prenom,
            @RequestParam(value = "ville", required = false) String ville,
            @RequestParam(value = "pays", required = false) String pays,
            @RequestParam(value = "telephone", required = false) String telephone,
            @RequestParam(value = "cvPath", required = false) String cvPath,
            @RequestParam(value = "photoProfil", required = false) MultipartFile photoProfil,
            @RequestParam(value = "certifications", required = false) String certificationsJson) {

        // Creer l'objet request
        UpdateCandidatProfileRequest request = new UpdateCandidatProfileRequest();
        request.setNom(nom);
        request.setPrenom(prenom);
        request.setVille(ville);
        request.setPays(pays);
        request.setTelephone(telephone);
        request.setCvPath(cvPath);
        request.setPhotoProfilFile(photoProfil);

        // Gérer les certifications si présentes
        if (certificationsJson != null && !certificationsJson.isEmpty()) {
            try {
                List<CertificationRequest> certifications = objectMapper.readValue(
                        certificationsJson,
                        new TypeReference<List<CertificationRequest>>() {}
                );
                request.setCertifications(certifications);
            } catch (JsonProcessingException e) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse("INVALID_CERTIFICATIONS", "Format des certifications invalide")
                );
            }
        }

        return candidatService.updateCandidatProfile(id, request);
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id && hasRole('CANDIDAT')")
    public ResponseEntity<?> updateMotDePasse(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequest request) {
        return candidatService.updateMotDePasse(
                id,
                request.getAncienMotDePasse(),
                request.getNouveauMotDePasse());
    }

    @GetMapping("/{id}/candidature")
    public ResponseEntity<List<CandidatureResponse>> getCandidatures(@PathVariable Long id) {
        return candidatService.getCandidatures(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id && hasRole('CANDIDAT')")
    public ResponseEntity<Void> deleteCandidat(@PathVariable Long id) {

        return candidatService.deleteCandidat(id);
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhotoProfil(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            String filename = fileStorageService.storeProfilePhoto(file, "candidat", id);
            candidatService.updatePhotoProfil(id, filename);
            return ResponseEntity.ok(new MessageResponse("Photo uploadée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("FILE_ERROR", e.getMessage()));
        }
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getPhotoProfil(@PathVariable Long id) {
        try {
            CandidatProfileResponse response = candidatService.getCandidatProfile(id).getBody();
            if (response == null || response.photoProfilPath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(fileStorageProperties.getUploadDir() + response.photoProfilPath());
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    private ResponseEntity<?> uploadCv(Long id, MultipartFile file) {
        try {
            String filename = fileStorageService.storeCv(file, id);
            candidatService.updateCv(id, filename);
            return ResponseEntity.ok(new MessageResponse("CV enregistré avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("CV_UPLOAD_ERROR", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cv/initial")
    public ResponseEntity<?> uploadInitialCv(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        // Vérifie si un CV existe déjà
        CandidatProfileResponse profile = candidatService.getCandidatProfile(id).getBody();
        if (profile != null && profile.cvPath() != null && !profile.cvPath().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("CV_EXISTS", "Un CV existe déjà pour ce candidat"));
        }


        return uploadCv(id, file);
    }

    @PatchMapping("/{id}/cv")
    public ResponseEntity<?> updateCv(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return uploadCv(id, file);
    }

    @PostMapping(value = "/{id}/certifications", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCertification(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("nom") String nom) {

        logger.info("Requête reçue - Candidat ID: {}, Nom Certification: {}, Taille Fichier: {}",
                id, nom, file.getSize());

        try {
            Certification certification = candidatService.addCertification(id, file, nom);

            CertificationResponse response = new CertificationResponse(
                    certification.getId(),
                    certification.getNom(),
                    certification.getPath());

            logger.info("Certification créée: {}", response);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur endpoint certification", e);
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("CERTIFICATION_ERROR", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/certifications/{certificationId}")
    public ResponseEntity<?> removeCertification(
            @PathVariable Long id,
            @PathVariable Long certificationId) {

        try {
            candidatService.removeCertification(certificationId);
            return ResponseEntity.ok(new MessageResponse("Certification supprimée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("CERTIFICATION_REMOVE_ERROR", e.getMessage()));
        }
    }

    @GetMapping("/{id}/certifications")
    public ResponseEntity<?> getCertifications(@PathVariable Long id) {
        try {
            List<Certification> certifications = candidatService.getCertifications(id);
            List<CertificationResponse> response = certifications.stream()
                    .map(c -> new CertificationResponse(c.getId(), c.getNom(), c.getPath()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Or use proper logging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }


    @GetMapping("/{id}/cv")
    public ResponseEntity<Resource> getCv(@PathVariable Long id) {
        try {
            // 1. Vérifier que le candidat existe et a un CV
            Candidat candidat = candidatRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

            if (candidat.getCvPath() == null || candidat.getCvPath().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // 2. Construire le chemin complet
            Path filePath = fileStorageService.getFilePath(candidat.getCvPath());
            Resource resource = new UrlResource(filePath.toUri());

            // 3. Verifier l'accès et le type de fichier
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // 4. Déterminer dynamiquement le content-Type (this is optionnal and we can remove it)
            String contentType = determineContentType(candidat.getCvPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String determineContentType(String filePath) {
        // Implémentation basique - à améliorer avec Files.probeContentType()
        if (filePath.toLowerCase().endsWith(".pdf")) {
            return "application/pdf";
        } else if (filePath.toLowerCase().endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

    @GetMapping("/{id}/certifications/{certificationId}/file")
    public ResponseEntity<Resource> getCertificationFile(
            @PathVariable Long id,
            @PathVariable Long certificationId) {

        try {
            // 1. Vérifier que le candidat existe
            if (!candidatRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // 2. Récuperer la certification
            Certification cert = certificationService.getCertification(certificationId);
            if (cert == null || !cert.getCandidat().getId().equals(id)) {
                return ResponseEntity.notFound().build();
            }

            // 3. Vérifier le fichier
            if (cert.getPath() == null || cert.getPath().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = fileStorageService.getFilePath(cert.getPath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = determineContentType(cert.getPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RECRUTEUR')")
    public ResponseEntity<List<CandidatCompleteResponse>> getAllCandidats() {
        return ResponseEntity.ok(candidatService.getAllCandidatsWithFiles());
    }
}