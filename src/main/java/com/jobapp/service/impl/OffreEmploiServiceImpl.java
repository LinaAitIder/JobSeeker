package com.jobapp.service.impl;

import com.jobapp.dto.response.OffreResponse;
import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.model.Candidature;
import com.jobapp.model.OffreEmploi;
import com.jobapp.repository.CandidatureRepository;
import com.jobapp.repository.OffreEmploiRepository;
import com.jobapp.repository.RecruteurRepository;
import com.jobapp.service.FileStorageService;
import com.jobapp.service.OffreEmploiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OffreEmploiServiceImpl implements OffreEmploiService {
    private static final Logger logger = LoggerFactory.getLogger(OffreEmploiServiceImpl.class);
    private final OffreEmploiRepository offreRepository;
    private final CandidatureRepository candidatureRepository;
    private final FileStorageService fileStorageService;
    private final RecruteurRepository recruteurRepository;

    public OffreEmploiServiceImpl(OffreEmploiRepository offreRepository,
                                  CandidatureRepository candidatureRepository,
                                  FileStorageService fileStorageService,
                                  RecruteurRepository recruteurRepository) {

        this.offreRepository = offreRepository;
        this.candidatureRepository = candidatureRepository;
        this.fileStorageService = fileStorageService;
        this.recruteurRepository = recruteurRepository;
    }


    @Override
    public ResponseEntity<OffreResponse> getOffreById(Long id) {
        OffreEmploi offre = offreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offre non trouvée"));
        return ResponseEntity.ok(mapToOffreResponse(offre));
    }

    @Override
    public ResponseEntity<List<OffreResponse>> getAllOffres() {
        List<OffreResponse> offres = offreRepository.findAll()
                .stream()
                .map(this::mapToOffreResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offres);
    }

    @Override
    public ResponseEntity<List<OffreResponse>> searchOffres(String searchTerm) {
        List<OffreResponse> offres = offreRepository.findByTitreContainingOrDescriptionContaining(searchTerm)
                .stream()
                .map(this::mapToOffreResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offres);
    }

    @Override
    public ResponseEntity<Void> deleteOffre(Long id) {
        try {
        offreRepository.deleteById(id);
        return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    @Transactional
    public void deleteOffreWithCandidatures(Long offreId) {
        // Récupère toutes les candidatures liées à l'offre
        List<Candidature> candidatures = candidatureRepository.findByOffreId(offreId);

        // Supprime les fichiers PDF associés
        candidatures.forEach(c -> {
            if (c.getLettreMotivationPath() != null) {
                try {
                    fileStorageService.deleteFile(c.getLettreMotivationPath());
                } catch (IOException e) {
                    logger.error("Erreur suppression lettre motivation pour candidature " + c.getId(), e);
                }
            }
        });

        // Supprime l'offre (la suppression en cascade s'occupera des candidatures)
        offreRepository.deleteById(offreId);
    }

    @Override
    public List<OffreResponse> getOffresByEntreprise(Long entrepriseId) {
        List<OffreEmploi> offres = offreRepository.findByRecruteurEntrepriseId(entrepriseId);

        return offres.stream()
                .map(this::mapToOffreResponse)
                .collect(Collectors.toList());
    }


    private OffreResponse mapToOffreResponse(OffreEmploi offre) {
        return new OffreResponse(
                offre.getId(),
                offre.getTitre(),
                offre.getDescription(),
                offre.getDomaine(),
                offre.getVille(),
                offre.getPays(),
                offre.getRecruteur().getId(),
                offre.getDatePublication(),
                offre.getDateExpiration(),
                offre.getSalaireMin(),
                offre.getSalaireMax(),
                offre.getTypeContrat()
        );
    }
}