package com.jobapp.service;

import com.jobapp.dto.response.OffreResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OffreEmploiService {
    ResponseEntity<OffreResponse> getOffreById(Long id);
    ResponseEntity<List<OffreResponse>> getAllOffres();
    ResponseEntity<List<OffreResponse>> searchOffres(String searchTerm);
    ResponseEntity<Void> deleteOffre(Long id);
    void deleteOffreWithCandidatures(Long offreId);
    List<OffreResponse> getOffresByEntreprise(Long entrepriseId);
    List<OffreResponse> getRecommendedOffers(Long candidateId);
    boolean hasCV(Long candidateId);

}