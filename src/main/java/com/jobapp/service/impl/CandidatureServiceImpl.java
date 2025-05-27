package com.jobapp.service.impl;

import com.jobapp.dto.exception.FileStorageException;
import com.jobapp.dto.request.CandidatureRequest;
import com.jobapp.dto.response.CandidatureResponse;
import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.model.Candidat;
import com.jobapp.model.Candidature;
import com.jobapp.model.OffreEmploi;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.CandidatureRepository;
import com.jobapp.repository.OffreEmploiRepository;
import com.jobapp.repository.RecruteurRepository;
import com.jobapp.service.CandidatureService;
import com.jobapp.service.FileStorageService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidatureServiceImpl implements CandidatureService {
    private static final Logger logger = LoggerFactory.getLogger(CandidatureServiceImpl.class);
    private final CandidatureRepository candidatureRepository;
    private final RecruteurRepository recruteurRepository;
    private final CandidatRepository candidatRepository;
    private final OffreEmploiRepository offreRepository;
    private final FileStorageService fileStorageService;

    public CandidatureServiceImpl(CandidatureRepository candidatureRepository,
                                  CandidatRepository candidatRepository,
                                  RecruteurRepository recruteurRepository,
                                  OffreEmploiRepository offreRepository,
                                  FileStorageService fileStorageService) {
        this.candidatureRepository = candidatureRepository;
        this.recruteurRepository = recruteurRepository;
        this.candidatRepository = candidatRepository;
        this.offreRepository = offreRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ResponseEntity<CandidatureResponse> postuler(Long candidatId, Long offreId, CandidatureRequest request) {
        try {
            // 1. Vérifier si le candidat a déjà postulé
            if (candidatureRepository.existsByCandidatIdAndOffreId(candidatId, offreId)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Vous avez déjà postulé à cette offre"
                );
            }

            // 2. Validation des entités existantes
            Candidat candidat = candidatRepository.findById(candidatId)
                    .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));
            OffreEmploi offre = offreRepository.findById(offreId)
                    .orElseThrow(() -> new NotFoundException("Offre non trouvée"));

            // 3. Gestion de la lettre de motivation...
            String lettrePath = null;
            if (request.getLettreMotivationFile() != null && !request.getLettreMotivationFile().isEmpty()) {
                if (!"application/pdf".equals(request.getLettreMotivationFile().getContentType())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seuls les fichiers PDF sont acceptés");
                }
                lettrePath = fileStorageService.storeLettreMotivation(
                        request.getLettreMotivationFile(),
                        candidatId,
                        offreId);

                // Ajoutez un log pour vérifier le chemin généré
                logger.info("Lettre de motivation stockée à : {}", lettrePath);
            }

            // 4. Création et sauvegarde
            Candidature candidature = new Candidature();
            candidature.setCandidat(candidat);
            candidature.setOffre(offre);
            candidature.setLettreMotivationPath(lettrePath);
            candidature.setMessageRecruteur(request.getMessageRecruteur());
            candidature.setDatePostulation(LocalDateTime.now());
            candidature.setStatut(Candidature.Statut.EN_ATTENTE);

            Candidature savedCandidature = candidatureRepository.save(candidature);
            return ResponseEntity.ok(mapToCandidatureResponse(savedCandidature));

        } catch (ResponseStatusException e) {
            throw e; // On relance les exceptions HTTP déjà gérées
        } catch (FileStorageException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur de stockage du fichier");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la candidature");
        }
    }

    @Override
    public ResponseEntity<List<CandidatureResponse>> getCandidaturesByOffre(Long offreId) {
        List<CandidatureResponse> candidatures = candidatureRepository.findByOffreId(offreId)
                .stream()
                .map(this::mapToCandidatureResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(candidatures);
    }

    @Override
    public ResponseEntity<CandidatureResponse> updateStatut(Long candidatureId, String newStatut) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new NotFoundException("Candidature non trouvée"));
        candidature.setStatut(Candidature.Statut.valueOf(newStatut));
        Candidature updated = candidatureRepository.save(candidature);
        return ResponseEntity.ok(mapToCandidatureResponse(updated));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteCandidature(Long id) {
        try {
            Candidature candidature = candidatureRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Candidature non trouvée"));

            // Suppression du fichier PDF si existant
            if (candidature.getLettreMotivationPath() != null) {
                try {
                    fileStorageService.deleteFile(candidature.getLettreMotivationPath());
                } catch (IOException e) {
                    logger.error("Erreur suppression lettre motivation", e);
                }
            }

            candidatureRepository.delete(candidature);
            return ResponseEntity.noContent().build();

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private CandidatureResponse mapToCandidatureResponse(Candidature candidature) {
        return new CandidatureResponse(
                candidature.getId(),
                candidature.getCandidat().getId(),
                candidature.getOffre().getId(),
                candidature.getDatePostulation(),
                candidature.getLettreMotivationPath(),
                candidature.getMessageRecruteur(),
                candidature.getStatut().name()
        );
    }

    public Candidature getById(Long id) {
        return candidatureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidature non trouvée"));
    }

    @Override
    public void deleteAllByCandidatId(Long candidatId) {
        // Récupère toutes les candidatures du candidat
        List<Candidature> candidatures = candidatureRepository.findByCandidatId(candidatId);

        // Supprime les fichiers physiques puis les entités en base
        for (Candidature candidature : candidatures) {
            try {
                if (candidature.getLettreMotivationPath() != null
                        && !candidature.getLettreMotivationPath().isEmpty()) {
                    fileStorageService.deleteFile(candidature.getLettreMotivationPath());
                }
            } catch (IOException e) {
                // Log l'erreur mais continue la suppression des autres fichiers
                System.err.println("Erreur lors de la suppression de la lettre de motivation: "
                        + candidature.getLettreMotivationPath() + " - " + e.getMessage());
            }
        }

        // Suppression en masse plus efficace que deleteAll()
        candidatureRepository.deleteByCandidatId(candidatId);
    }

    @Override
    public ResponseEntity<List<CandidatureResponse>> getAllCandidaturesForRecruteur(Long recruteurId, String statut) {
        try {
            // 1. Vérifier que le recruteur existe
            if (!recruteurRepository.existsById(recruteurId)) {
                return ResponseEntity.notFound().build();
            }

            // 2. Recupérer toutes les offres du recruteur
            List<OffreEmploi> offres = offreRepository.findByRecruteurId(recruteurId);
            List<Long> offreIds = offres.stream().map(OffreEmploi::getId).collect(Collectors.toList());

            // 3. Récupérer les candidatures selon le statut
            List<Candidature> candidatures;
            if (statut != null && !statut.isEmpty()) {
                Candidature.Statut statutEnum = Candidature.Statut.valueOf(statut.toUpperCase());
                candidatures = candidatureRepository.findByOffreIdInAndStatutOrderByDatePostulationDesc(offreIds, statutEnum);
            } else {
                candidatures = candidatureRepository.findByOffreIdInOrderByDatePostulationDesc(offreIds);
            }

            // 4. Mapper vers les DTO de response
            List<CandidatureResponse> responses = candidatures.stream()
                    .map(this::mapToCandidatureResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}