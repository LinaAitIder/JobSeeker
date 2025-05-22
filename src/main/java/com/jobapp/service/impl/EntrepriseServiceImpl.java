package com.jobapp.service.impl;

import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.dto.exception.ServiceException;
import com.jobapp.dto.request.EntrepriseRequest;
import com.jobapp.dto.response.EntrepriseResponse;
import com.jobapp.model.Entreprise;
import com.jobapp.repository.EntrepriseDAO;
import com.jobapp.service.EntrepriseService;
import com.jobapp.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EntrepriseServiceImpl implements EntrepriseService {

    private static final Logger logger = LoggerFactory.getLogger(EntrepriseServiceImpl.class);
    private final EntrepriseDAO entrepriseDAO;
    private final FileStorageService fileStorageService;

    @Autowired
    public EntrepriseServiceImpl(EntrepriseDAO entrepriseDAO,
                                 FileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
        this.entrepriseDAO = entrepriseDAO;
    }

    @Override
    public EntrepriseResponse createEntreprise(EntrepriseRequest request) {
        // Conversion sécurisée de la taille
        Entreprise.TailleEntreprise taille;
        try {
            taille = request.getTaille();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Taille d'entreprise invalide. Options valides : " +
                            Arrays.toString(Entreprise.TailleEntreprise.values()));
        }

        Entreprise entreprise = new Entreprise(
                request.getNom(),
                request.getDescription(),
                request.getLocation(),
                taille,
                request.getDomaine()
        );

        Entreprise saved = entrepriseDAO.save(entreprise);
        return new EntrepriseResponse(saved);
    }

    @Override
    public EntrepriseResponse updateLogo(Long entrepriseId, MultipartFile file) {
        Entreprise entreprise = entrepriseDAO.findById(entrepriseId)
                .orElseThrow(() -> new NotFoundException("Entreprise non trouvée"));

        String logoPath = fileStorageService.storeCompanyLogo(file, entrepriseId);
        entreprise.setLogoPath(logoPath);

        return new EntrepriseResponse(entrepriseDAO.save(entreprise));
    }

    @Override
    public EntrepriseResponse getEntrepriseById(Long id) {
        Entreprise entreprise = entrepriseDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Entreprise non trouvée avec l'ID : " + id));
        return new EntrepriseResponse(entreprise);
    }

    @Override
    public List<EntrepriseResponse> getAllEntreprises() {
        return entrepriseDAO.findAll()
                .stream()
                .map(EntrepriseResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Entreprise findOrCreate(String nomEntreprise) {
        try {
            // Gestion du cas null
            if (nomEntreprise == null || nomEntreprise.trim().isEmpty()) {
                nomEntreprise = "Entreprise non spécifiée";
            }

            String nomNormalise = nomEntreprise.trim();

            // Chercher d'abord avec une correspondance exacte
            Optional<Entreprise> existanteExacte = entrepriseDAO.findByNom(nomNormalise);
            if (existanteExacte.isPresent()) {
                return existanteExacte.get();
            }

            // Si pas trouvé, chercher avec LIKE
            Optional<Entreprise> existanteApproximative = entrepriseDAO.findByNomContaining(nomNormalise);
            if (existanteApproximative.isPresent()) {
                return existanteApproximative.get();
            }

            // Si aucune entreprise trouvée, creer une nouvelle
            Entreprise nouvelleEntreprise = new Entreprise();
            nouvelleEntreprise.setNom(nomNormalise);
            nouvelleEntreprise.setTaille(Entreprise.TailleEntreprise.MOYENNE);
            nouvelleEntreprise.setDomaine("Non spécifié");
            nouvelleEntreprise.setLocation("Non spécifié");
            nouvelleEntreprise.setDescription(nomNormalise);

            return entrepriseDAO.save(nouvelleEntreprise);

        } catch (Exception e) {
            logger.error("Erreur lors de la recherche/création d'entreprise", e);
            throw new ServiceException("Erreur lors du traitement de l'entreprise");
        }
    }

    public Optional<Entreprise> findByNom(String nom) {
        return entrepriseDAO.findByNom(nom);
    }
}