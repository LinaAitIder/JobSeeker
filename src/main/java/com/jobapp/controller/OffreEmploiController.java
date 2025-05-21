package com.jobapp.controller;

import com.jobapp.dto.response.OffreResponse;
import com.jobapp.service.OffreEmploiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
public class OffreEmploiController {

    private final OffreEmploiService offreService;
    private static final Logger log = LoggerFactory.getLogger(OffreEmploiController.class);

    public OffreEmploiController(OffreEmploiService offreService) {
        this.offreService = offreService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffreResponse> getOffreById(@PathVariable Long id) {
        return offreService.getOffreById(id);
    }

    @GetMapping
    public ResponseEntity<List<OffreResponse>> getAllOffres() {
        return offreService.getAllOffres();
    }

    @GetMapping("/search")
    public ResponseEntity<List<OffreResponse>> searchOffres(@RequestParam String q) {
        return offreService.searchOffres(q); }

    @GetMapping("/entreprise/{entrepriseId}")
    public ResponseEntity<List<OffreResponse>> getOffresByEntreprise(
            @PathVariable Long entrepriseId) {
        return ResponseEntity.ok(offreService.getOffresByEntreprise(entrepriseId));}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable Long id) {
        return offreService.deleteOffre(id);
    }

    @GetMapping("/has-cv/{candidateId}")
    public ResponseEntity<Boolean> checkCV(@PathVariable Long candidateId) {
        try {
            if (candidateId == null || candidateId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(offreService.hasCV(candidateId));
        } catch (Exception e) {
            log.error("Error checking CV status for candidate {}", candidateId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/recommended/{candidateId}")
    public ResponseEntity<List<OffreResponse>> getRecommendedOffers(
            @PathVariable Long candidateId) {
        try {
            if (candidateId == null || candidateId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            List<OffreResponse> offers = offreService.getRecommendedOffers(candidateId);
            return ResponseEntity.ok(offers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        } catch (Exception e) {
            log.error("Error getting recommended offers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}