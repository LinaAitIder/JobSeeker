package com.jobapp.service;

import com.jobapp.dto.request.CreateOffreRequest;
import com.jobapp.dto.request.UpdateRecruteurProfileRequest;
import com.jobapp.dto.response.CandidatureResponse;
import com.jobapp.dto.response.OffreResponse;
import com.jobapp.dto.response.RecruteurProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecruteurService {
    ResponseEntity<RecruteurProfileResponse> getRecruteurProfile(Long id);
    ResponseEntity<List<CandidatureResponse>> getCandidaturesPourOffre(Long offreId, String statut);
    ResponseEntity<?> updateRecruteurProfile(Long id, UpdateRecruteurProfileRequest request);
    ResponseEntity<List<OffreResponse>> getOffresByRecruteur(Long recruteurId);
    ResponseEntity<OffreResponse> createOffre(Long recruteurId, CreateOffreRequest request);
    ResponseEntity<Void> deleteRecruteur(Long id);
    ResponseEntity<?> updateMotDePasse(Long id,
                                       String ancienMotDePasse,
                                       String nouveauMotDePasse);
    void updatePhotoProfil(Long id, String filename);
}