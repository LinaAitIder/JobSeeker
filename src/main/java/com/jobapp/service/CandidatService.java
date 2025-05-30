package com.jobapp.service;

import com.jobapp.dto.request.UpdateCandidatProfileRequest;
import com.jobapp.dto.response.CandidatCompleteResponse;
import com.jobapp.dto.response.CandidatProfileResponse;
import com.jobapp.dto.response.CandidatureResponse;
import com.jobapp.model.Certification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidatService {
    ResponseEntity<CandidatProfileResponse> getCandidatProfile(Long id);

    ResponseEntity<?> updateCandidatProfile(Long id, UpdateCandidatProfileRequest request);

    ResponseEntity<List<CandidatureResponse>> getCandidatures(Long candidatId);

    ResponseEntity<Void> deleteCandidat(Long id);

    ResponseEntity<?> updateMotDePasse(Long id,
                                       String ancienMotDePasse,
                                       String nouveauMotDePasse);

    void updatePhotoProfil(Long id, String filename);

    void updateCv(Long id, String cvPath);

    Certification addCertification(Long id, MultipartFile file, String nom);

    void removeCertification(Long certificationId);

    List<Certification> getCertifications(Long candidatId);

    List<CandidatCompleteResponse> getAllCandidatsWithFiles();

}