package com.jobapp.controller;

import com.jobapp.dto.request.CandidatureRequest;
import com.jobapp.dto.response.CandidatureResponse;
import com.jobapp.service.CandidatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidature")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
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
}