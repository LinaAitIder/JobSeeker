package com.jobapp.controller;

import com.jobapp.dto.response.OffreResponse;
import com.jobapp.service.OffreEmploiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
public class OffreEmploiController {

    private final OffreEmploiService offreService;

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
}