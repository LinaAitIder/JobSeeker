package com.jobapp.service.impl;

import com.jobapp.dto.request.CreateOffreRequest;
import com.jobapp.dto.request.UpdateRecruteurProfileRequest;
import com.jobapp.dto.response.*;
import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.model.Candidature;
import com.jobapp.model.Entreprise;
import com.jobapp.model.Recruteur;
import com.jobapp.model.OffreEmploi;
import com.jobapp.repository.CandidatureRepository;
import com.jobapp.repository.OffreEmploiRepository;
import com.jobapp.repository.RecruteurRepository;
import com.jobapp.service.EntrepriseService;
import com.jobapp.service.FileStorageService;
import com.jobapp.service.RecruteurService;
import com.jobapp.dto.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecruteurServiceImpl implements RecruteurService {

    private static final Logger logger = LoggerFactory.getLogger(RecruteurServiceImpl.class);
    private final RecruteurRepository recruteurRepository;
    private final OffreEmploiRepository offreRepository;
    private final CandidatureRepository candidatureRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    private EntrepriseService entrepriseService;


    @Autowired
    public RecruteurServiceImpl(RecruteurRepository recruteurRepository,
                                OffreEmploiRepository offreRepository,
                                CandidatureRepository candidatureRepository, FileStorageService fileStorageService) {
        this.recruteurRepository = recruteurRepository;
        this.offreRepository = offreRepository;
        this.candidatureRepository = candidatureRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ResponseEntity<RecruteurProfileResponse> getRecruteurProfile(Long id) {
        Recruteur recruteur = recruteurRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

        return ResponseEntity.ok(mapToRecruteurProfileResponse(recruteur));
    }

    @Override
    public ResponseEntity<?> updateMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse) {
        try {
            Recruteur recruteur = recruteurRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

            if (!recruteur.getMotDePasse().equals(ancienMotDePasse)) {
                return ResponseEntity.status(401)
                        .body(new ErrorResponse("AUTH_ERROR", "Ancien mot de passe incorrect"));
            }

            if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("VALIDATION_ERROR",
                                "Le mot de passe doit contenir au moins 6 caractères"));
            }

            recruteur.setMotDePasse(nouveauMotDePasse);
            recruteurRepository.save(recruteur);

            return ResponseEntity.ok(new MessageResponse("Mot de passe mis à jour avec succès"));

        } catch (NotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> updateRecruteurProfile(Long id, UpdateRecruteurProfileRequest request) {
        try {
            Recruteur recruteur = recruteurRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

            // Mise à jour des champs
            if (request.getNom() != null) recruteur.setNom(request.getNom());
            if (request.getPrenom() != null) recruteur.setPrenom(request.getPrenom());
            if (request.getPosition() != null) recruteur.setPosition(request.getPosition());
            if (request.getTelephone() != null) recruteur.setTelephone(request.getTelephone());

            // Gestion de la photo de profil
            if (request.getPhotoProfilFile() != null && !request.getPhotoProfilFile().isEmpty()) {
                // Validation du type de fichier
                String contentType = request.getPhotoProfilFile().getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Seules les images sont acceptées pour la photo de profil"
                    );
                }

                // Suppression de l'ancienne photo si elle existe
                if (recruteur.getPhotoProfilPath() != null) {
                    try {
                        fileStorageService.deleteFile(recruteur.getPhotoProfilPath());
                    } catch (IOException e) {
                        logger.error("Échec de suppression de l'ancienne photo de profil", e);
                    }
                }

                // Stockage de la nouvelle photo
                String photoPath = fileStorageService.storeProfilePhoto(
                        request.getPhotoProfilFile(),
                        "recruteur",
                        id
                );
                recruteur.setPhotoProfilPath(photoPath);
            }

            // Gestion de l'entreprise via son nom
            if (request.getEntrepriseNom() != null) {
                Entreprise entreprise = entrepriseService.findOrCreate(request.getEntrepriseNom());
                recruteur.setEntreprise(entreprise);
            }

            recruteurRepository.save(recruteur);
            return ResponseEntity.ok(mapToRecruteurProfileResponse(recruteur));

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la mise à jour du profil");
        }
    }

    @Override
    public ResponseEntity<List<CandidatureResponse>> getCandidaturesPourOffre(Long offreId, String statut) {
        try {
            // 1. Vérifier que l'offre existe
            OffreEmploi offre = offreRepository.findById(offreId)
                    .orElseThrow(() -> new NotFoundException("Offre non trouvée"));

            // 2. Récupérer les candidatures selon le statut
            List<Candidature> candidatures;
            if (statut != null && !statut.isEmpty()) {
                Candidature.Statut statutEnum = Candidature.Statut.valueOf(statut.toUpperCase());
                candidatures = candidatureRepository.findByOffreIdAndStatut(offreId, statutEnum);
            } else {
                candidatures = candidatureRepository.findByOffreId(offreId);
            }

            // 3. Mapper vers les DTO de réponse
            List<CandidatureResponse> responses = candidatures.stream()
                    .map(c -> new CandidatureResponse(
                            c.getId(),
                            c.getCandidat().getId(),
                            c.getOffre().getId(),
                            c.getDatePostulation(),
                            c.getLettreMotivationPath(),
                            c.getMessageRecruteur(),
                            c.getStatut().name()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            // Gestion du cas ou le statut n'est pas valide
            // Retourne une liste vide avec un statut BAD_REQUEST
            return ResponseEntity.badRequest().body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Override
    public ResponseEntity<List<OffreResponse>> getOffresByRecruteur(Long recruteurId) {
        try {
            List<OffreEmploi> offres = offreRepository.findByRecruteurId(recruteurId);

            List<OffreResponse> responses = offres.stream()
                    .map(this::mapToOffreResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
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

    @Override
    public ResponseEntity<OffreResponse> createOffre(Long recruteurId, CreateOffreRequest request) {
        try {
            // 1. Vérifier que le recruteur existe
            Recruteur recruteur = recruteurRepository.findById(recruteurId)
                    .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

            // 2. Valider les données de la requête
            if (request.getTitre() == null || request.getTitre().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new OffreResponse(null, null, "Le titre est obligatoire", null, null, null, null, null, null, null, null, null));
            }

            if (request.getDateExpiration() != null && request.getDateExpiration().isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest()
                        .body(new OffreResponse(null, null, "La date d'expiration doit être dans le futur", null, null, null, null, null, null, null, null, null));
            }

            // 3. Créer et sauvegarder l'offre
            OffreEmploi offre = new OffreEmploi();
            offre.setTitre(request.getTitre());
            offre.setDescription(request.getDescription());
            offre.setDomaine(request.getDomaine());
            offre.setVille(request.getVille());
            offre.setPays(request.getPays());
            offre.setRecruteur(recruteur);
            offre.setDatePublication(LocalDate.now());
            offre.setDateExpiration(request.getDateExpiration());
            offre.setSalaireMin(request.getSalaireMin());
            offre.setSalaireMax(request.getSalaireMax());
            offre.setTypeContrat(request.getTypeContrat());

            OffreEmploi savedOffre = offreRepository.save(offre);

            // 4. Retourner la réponse
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapToOffreResponse(savedOffre));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new OffreResponse(null, null, e.getMessage(), null, null, null, null, null, null, null, null, null));
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'offre", e);
            return ResponseEntity.internalServerError()
                    .body(new OffreResponse(null, null, "Erreur interne du serveur", null, null, null, null, null, null, null, null, null));
        }
    }

    private RecruteurProfileResponse mapToRecruteurProfileResponse(Recruteur recruteur) {
        return new RecruteurProfileResponse(
                recruteur.getId(),
                recruteur.getNom(),
                recruteur.getPrenom(),
                recruteur.getEmail(),
                recruteur.getEntreprise() != null ? recruteur.getEntreprise().getNom() : null,
                recruteur.getPosition(),
                recruteur.getTelephone(),
                recruteur.getPhotoProfilPath()
        );
    }

    @Override
    public ResponseEntity<EntrepriseResponse> getEntrepriseByRecruteurId(Long recruteurId) {
        Recruteur recruteur = recruteurRepository.findById(recruteurId)
                .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

        if (recruteur.getEntreprise() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new EntrepriseResponse(recruteur.getEntreprise()));
    }

    @Override
    public void updatePhotoProfil(Long id, String filename) {
        try {
            Recruteur recruteur = recruteurRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

            if (recruteur.getPhotoProfilPath() != null) {
                try {
                    fileStorageService.deleteFile(recruteur.getPhotoProfilPath());
                } catch (IOException e) {
                    logger.error("Erreur suppression ancienne photo: " + e.getMessage());
                }
            }
            recruteurRepository.updatePhotoProfil(id, filename);
        } catch (Exception e) {
            throw new ServiceException("Échec mise à jour photo profil", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRecruteur(Long id) {
        try {
            // 1. Trouver le recruteur
            Recruteur recruteur = recruteurRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

            // 2. Supprimer d'abord les candidatures liees aux offres
            List<OffreEmploi> offres = offreRepository.findByRecruteurId(id);
            offres.forEach(offre -> candidatureRepository.deleteByOffreId(offre.getId()));

            // 3. Supprimer les offres d'emploi
            offreRepository.deleteByRecruteurId(id);

            // 4. Supprimer les fichiers
            deleteFileSafely(recruteur.getPhotoProfilPath());

            // 5. Supprimer le recruteur
            recruteurRepository.delete(recruteur);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du recruteur ID: " + id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Méthode helper pour suppression sécurisée des fichiers
    private void deleteFileSafely(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                fileStorageService.deleteFile(filePath);
            } catch (IOException e) {
                logger.warn("Échec de suppression du fichier {} : {}", filePath, e.getMessage());
            }
        }
    }
}