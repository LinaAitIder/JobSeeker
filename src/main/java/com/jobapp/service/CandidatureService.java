package com.jobapp.service;

import com.jobapp.dto.request.CandidatureRequest;
import com.jobapp.dto.response.CandidatureCompleteResponse;
import com.jobapp.dto.response.CandidatureResponse;
import com.jobapp.model.Candidature;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CandidatureService {
    ResponseEntity<CandidatureResponse> postuler(Long candidatId, Long offreId, CandidatureRequest request);
    ResponseEntity<List<CandidatureResponse>> getCandidaturesByOffre(Long offreId);
    ResponseEntity<CandidatureResponse> updateStatut(Long candidatureId, String newStatut);
    ResponseEntity<Void> deleteCandidature(Long id);
    void deleteAllByCandidatId(Long candidatId);
    Candidature getById(Long id);
    ResponseEntity<List<CandidatureCompleteResponse>> getAllCandidaturesForRecruteur(Long recruteurId, String statut);
}