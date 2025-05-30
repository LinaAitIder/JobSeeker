package com.jobapp.controller;

import com.jobapp.config.FileStorageProperties;
import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.dto.request.CandidatureRequest;
import com.jobapp.dto.response.CandidatProfileResponse;
import com.jobapp.dto.response.CandidatureResponse;
import com.jobapp.model.Candidature;
import com.jobapp.repository.CandidatureRepository;
import com.jobapp.service.CandidatService;
import com.jobapp.service.CandidatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/candidature")
public class CandidatureController {
    private static final Logger logger = LoggerFactory.getLogger(CandidatureController.class);
    private final CandidatureService candidatureService;
    private final FileStorageProperties fileStorageProperties;
    private final CandidatService candidatService;
    private final CandidatureRepository candidatureRepository;

    public CandidatureController(CandidatureService candidatureService,
                                 FileStorageProperties fileStorageProperties,
                                 CandidatService candidatService,
                                 CandidatureRepository candidatureRepository) {
        this.candidatureService = candidatureService;
        this.fileStorageProperties = fileStorageProperties;
        this.candidatureRepository = candidatureRepository;
        this.candidatService = candidatService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CandidatureResponse> postuler(
            @RequestParam("candidatId") Long candidatId,
            @RequestParam("offreId") Long offreId,
            @RequestParam(value = "lettreMotivation", required = false) MultipartFile lettreMotivation,
            @RequestParam(value = "messageRecruteur", required = false) String messageRecruteur) {

        CandidatureRequest request = new CandidatureRequest();
        request.setOffreId(offreId);
        request.setLettreMotivationFile(lettreMotivation);
        request.setMessageRecruteur(messageRecruteur);

        return candidatureService.postuler(candidatId, offreId, request);
    }

    @GetMapping("/offre/{offreId}")
    public ResponseEntity<List<CandidatureResponse>> getCandidaturesByOffre(@PathVariable Long offreId) {
        return candidatureService.getCandidaturesByOffre(offreId);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<CandidatureResponse> updateStatut(
            @PathVariable Long id,
            @RequestParam String newStatut) {
        return candidatureService.updateStatut(id, newStatut);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidature(@PathVariable Long id) {
        return candidatureService.deleteCandidature(id);
    }

    @GetMapping("/{candidatureId}/lettre-motivation")
    public ResponseEntity<Resource> getLettreMotivation(@PathVariable Long candidatureId) {
        try {
            Candidature candidature = candidatureService.getById(candidatureId);
            String pathStr = candidature.getLettreMotivationPath();

            if (pathStr == null || pathStr.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(fileStorageProperties.getUploadDir() + pathStr);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erreur récupération lettre motivation ID: " + candidatureId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{candidatureId}/candidat-profile")
    @PreAuthorize("hasRole('RECRUTEUR')")
    public ResponseEntity<CandidatProfileResponse> getCandidatProfilFromCandidature(
            @PathVariable Long candidatureId) {

            //Récupérer la candidature
            Candidature candidature = candidatureRepository.findById(candidatureId)
                    .orElseThrow(() -> new NotFoundException("Candidature non trouvée"));

            return candidatService.getCandidatProfile(candidature.getCandidat().getId());
    }

}