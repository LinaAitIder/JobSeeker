package com.jobapp.service;

import com.jobapp.dto.request.EntrepriseRequest;
import com.jobapp.dto.response.EntrepriseResponse;
import com.jobapp.model.Entreprise;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface EntrepriseService {
    EntrepriseResponse createEntreprise(EntrepriseRequest request);
    EntrepriseResponse updateLogo(Long entrepriseId, MultipartFile file);
    List<EntrepriseResponse> getAllEntreprises();
    EntrepriseResponse getEntrepriseById(Long id);
    Entreprise findOrCreate(String nomEntreprise);
    Optional<Entreprise> findByNom(String nom);
}
