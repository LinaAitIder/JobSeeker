package com.jobapp.controller;

import com.jobapp.config.FileStorageProperties;
import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.dto.request.CandidatureRequest;
import com.jobapp.dto.response.CandidatureResponse;
import com.jobapp.model.Candidature;
import com.jobapp.service.CandidatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/candidature")
public class CandidatureController {
    private static final Logger logger = LoggerFactory.getLogger(CandidatureController.class);
    private final CandidatureService candidatureService;
    private final FileStorageProperties fileStorageProperties;

    public CandidatureController(CandidatureService candidatureService,
                                 FileStorageProperties fileStorageProperties) {
        this.candidatureService = candidatureService;
        this.fileStorageProperties = fileStorageProperties;
    }

    @PostMapping
    public ResponseEntity<CandidatureResponse> postuler(
            @RequestParam Long candidatId,
            @RequestParam Long offreId,
            @RequestBody CandidatureRequest request) {
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
}