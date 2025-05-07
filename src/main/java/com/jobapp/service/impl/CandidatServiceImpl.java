package com.jobapp.service.impl;

import com.jobapp.dto.request.CertificationRequest;
import com.jobapp.dto.request.UpdateCandidatProfileRequest;
import com.jobapp.dto.response.CandidatProfileResponse;
import com.jobapp.dto.response.CandidatureResponse;
import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.dto.exception.ServiceException;
import com.jobapp.dto.response.ErrorResponse;
import com.jobapp.dto.response.MessageResponse;
import com.jobapp.model.Candidat;
import com.jobapp.model.Candidature;
import com.jobapp.model.Certification;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.CandidatureRepository;
import com.jobapp.repository.CertificationRepository;
import com.jobapp.service.CandidatService;
import com.jobapp.service.CandidatureService;
import com.jobapp.service.CertificationService;
import com.jobapp.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidatServiceImpl implements CandidatService {
    private static final Logger logger = LoggerFactory.getLogger(CandidatServiceImpl.class);
    private final CandidatRepository candidatRepository;
    private final CandidatureRepository candidatureRepository;
    private final CertificationRepository certificationRepository;
    private final FileStorageService fileStorageService;
    @Autowired
    private final CertificationService certificationService;
    @Autowired
    private final CandidatureService candidatureService;

    @Autowired
    public CandidatServiceImpl(CandidatRepository candidatRepository,
                               CandidatureRepository candidatureRepository,
                               CertificationRepository certificationRepository,
                               FileStorageService fileStorageService,
                               CertificationService certificationService,
                               CandidatureService candidatureService) {
        this.candidatRepository = candidatRepository;
        this.candidatureRepository = candidatureRepository;
        this.certificationRepository = certificationRepository;
        this.fileStorageService = fileStorageService;
        this.certificationService = certificationService;
        this.candidatureService = candidatureService;
    }

    @Override
    public ResponseEntity<CandidatProfileResponse> getCandidatProfile(Long id) {
        Candidat candidat = candidatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

        return ResponseEntity.ok(mapToCandidatProfileResponse(candidat));
    }

    @Override
    public ResponseEntity<?> updateCandidatProfile(Long id, UpdateCandidatProfileRequest request) {
        try {
            Candidat candidat = candidatRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

            // Mise à jour des champs simples
            if (request.getNom() != null) candidat.setNom(request.getNom());
            if (request.getPrenom() != null) candidat.setPrenom(request.getPrenom());
            if (request.getTelephone() != null) candidat.setTelephone(request.getTelephone());
            if (request.getVille() != null) candidat.setVille(request.getVille());
            if (request.getPays() != null) candidat.setPays(request.getPays());
            if (request.getCvPath() != null) candidat.setCvPath(request.getCvPath());
            if (request.getPhotoProfilPath() != null) candidat.setPhotoProfilPath(request.getPhotoProfilPath());

            // Gestion des certifications
            if (request.getCertifications() != null) {
                manageCertifications(candidat, request.getCertifications());
            }

            Candidat updated = candidatRepository.save(candidat);
            return ResponseEntity.ok(mapToCandidatProfileResponse(updated));

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("NOT_FOUND", e.getMessage()));
        }
    }

    private void manageCertifications(Candidat candidat, List<CertificationRequest> certRequests) {
        // 1. Convertir les CertificationRequest en Set pour comparaison
        Set<CertificationRequest> requestSet = certRequests.stream()
                .map(req -> new CertificationRequest(req.getNom(), req.getPath()))
                .collect(Collectors.toSet());

        // 2. Parcourir les certifications existantes
        Iterator<Certification> iterator = candidat.getCertifications().iterator();
        while (iterator.hasNext()) {
            Certification existingCert = iterator.next();
            CertificationRequest certKey = new CertificationRequest(existingCert.getNom(), existingCert.getPath());

            // Supprimer si absente de la requête
            if (!requestSet.contains(certKey)) {
                iterator.remove();
                certificationRepository.delete(existingCert);
            } else {
                // Retirer du set pour ne pas la recréer
                requestSet.remove(certKey);
            }
        }

        // 3. Ajouter les nouvelles certifications
        requestSet.forEach(req -> {
            Certification newCert = new Certification();
            newCert.setNom(req.getNom());
            newCert.setPath(req.getPath());
            newCert.setCandidat(candidat);
            candidat.getCertifications().add(newCert);
        });
    }

    @Override
    public ResponseEntity<List<CandidatureResponse>> getCandidatures(Long candidatId) {
        try {
            // 1. Verifier que le candidat existe (maybe we won't use it)
            if (!candidatRepository.existsById(candidatId)) {
                throw new NotFoundException("Candidat non trouvé");
            }

            // 2. Recuperer les candidatures en utilisant candidatId
            List<Candidature> candidatures = candidatureRepository.findByCandidatId(candidatId);

            // 3. Mapper vers les DTO de reponse
            List<CandidatureResponse> responses = candidatures.stream()
                    .map(this::mapToCandidatureResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
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

    @Override
    @Transactional
    public ResponseEntity<Void> deleteCandidat(Long id) {
        try {
            // 1. Vérifier que le candidat existe
            logger.info("Suppression du candidat ID: {}", id);
            Candidat candidat = candidatRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

            // 2. Supprimer d'abord les dépendances
            logger.info("Suppression des certifications...");
            if (certificationRepository != null) {
                List<Certification> certifications = certificationRepository.findByCandidatId(id);
                if (!certifications.isEmpty()) {
                    certificationRepository.deleteByCandidatId(id);
                }
            }

            logger.info("Suppression des candidatures...");
            candidatureService.deleteAllByCandidatId(id);

            // 3. Supprimer les fichiers physiques
            deleteFileSafely(candidat.getCvPath());
            deleteFileSafely(candidat.getPhotoProfilPath());

            // 4. Supprimer le candidat
            logger.info("Suppression finale du candidat...");
            candidatRepository.deleteById(id);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            logger.error("Échec de la suppression du candidat ID: {}", id, e);
            throw e; // Propager l'exception pour voir la 500 dans les logs
        }
    }

    // Méthode helper pour la suppression sécurisée des fichiers
    private void deleteFileSafely(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                fileStorageService.deleteFile(filePath);
            } catch (IOException e) {
                // Log l'erreur mais continue l'exécution
                logger.error("Échec de suppression du fichier {} : {}", filePath, e.getMessage());
            }
        }
    }

    private CandidatProfileResponse mapToCandidatProfileResponse(Candidat candidat) {
        return new CandidatProfileResponse(
                candidat.getId(),
                candidat.getNom(),
                candidat.getPrenom(),
                candidat.getEmail(),
                candidat.getVille(),
                candidat.getPays(),
                candidat.getTelephone(),
                candidat.getCertifications(),
                candidat.getCvPath(),
                candidat.getPhotoProfilPath()
        );
    }
    @Override
    public ResponseEntity<?> updateMotDePasse(Long id,
                                              String ancienMotDePasse,
                                              String nouveauMotDePasse) {
        try {
            // 1. Vérifier que le candidat existe
            Candidat candidat = candidatRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

            // 2. Vérifier l'ancien mot de passe
            if (!candidat.getMotDePasse().equals(ancienMotDePasse)) {
                return ResponseEntity.status(401)
                        .body(new ErrorResponse("AUTH_ERROR", "Ancien mot de passe incorrect"));
            }

            // 3. Valider le nouveau mot de passe
            if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("VALIDATION_ERROR",
                                "Le mot de passe doit contenir au moins 6 caractères"));
            }

            // 4. Mettre a jour le mot de passe
            candidat.setMotDePasse(nouveauMotDePasse);
            candidatRepository.save(candidat);

            return ResponseEntity.ok(new MessageResponse("Mot de passe mis à jour avec succès"));

        } catch (NotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("SERVER_ERROR",
                            "Erreur lors de la mise à jour du mot de passe"));
        }
    }
    @Override
    public void updatePhotoProfil(Long id, String filename) {
        try {
            Candidat candidat = candidatRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

            if (candidat.getPhotoProfilPath() != null) {
                try {
                    fileStorageService.deleteFile(candidat.getPhotoProfilPath());
                } catch (IOException e) {
                    logger.error("Erreur suppression ancienne photo: " + e.getMessage());
                }
            }
            candidatRepository.updatePhotoProfil(id, filename);
        } catch (Exception e) {
            throw new ServiceException("Échec mise à jour photo profil", e);
        }
    }
    @Override
    public void updateCv(Long id, String cvPath) {
        try {
            Candidat candidat = candidatRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

            if (candidat.getCvPath() != null) {
                fileStorageService.deleteFile(candidat.getCvPath());
            }
            candidatRepository.updateCvPath(id, cvPath);
        } catch (IOException e) {
            throw new ServiceException("Erreur suppression ancien CV", e);
        }
    }

    @Override
    public Certification addCertification(Long id, MultipartFile file, String nom) {
        String filePath = fileStorageService.storeFile(file, "certifications", id);
        return certificationService.addCertification(id, filePath, nom);
    }

    @Override
    public void removeCertification(Long certificationId) {
        certificationService.removeCertification(certificationId);
    }

    @Override
    public List<Certification> getCertifications(Long candidatId) {
        return certificationService.getCertificationsByCandidatId(candidatId);
    }

}