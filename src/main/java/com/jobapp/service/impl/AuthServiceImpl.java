package com.jobapp.service.impl;

import com.jobapp.dto.exception.AuthenticationException;
import com.jobapp.dto.exception.DuplicateEmailException;
import com.jobapp.dto.exception.DuplicatePasswordException;
import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.dto.request.*;
import com.jobapp.dto.response.*;
import com.jobapp.model.Candidat;
import com.jobapp.model.Certification;
import com.jobapp.model.Entreprise;
import com.jobapp.model.Recruteur;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.RecruteurRepository;
import com.jobapp.service.AuthService;
import com.jobapp.service.EntrepriseService;
import com.jobapp.service.FileStorageService;
import com.jobapp.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final CandidatRepository candidatRepository;
    private final RecruteurRepository recruteurRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntrepriseService entrepriseService;
    private final FileStorageService fileStorageService;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(CandidatRepository candidatRepository,
                           RecruteurRepository recruteurRepository,
                           PasswordEncoder passwordEncoder,
                           EntrepriseService entrepriseService,
                           FileStorageService fileStorageService,
                           JwtUtil jwtUtil) {
        this.candidatRepository = candidatRepository;
        this.recruteurRepository = recruteurRepository;
        this.passwordEncoder = passwordEncoder;
        this.entrepriseService = entrepriseService;
        this.fileStorageService = fileStorageService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse authenticate(LoginRequest loginRequest) throws AuthenticationException {
        Optional<Candidat> candidatOpt = candidatRepository.findByEmail(loginRequest.getEmail());
        if (candidatOpt.isPresent()) {
            Candidat candidat = candidatOpt.get();
            if (passwordEncoder.matches(loginRequest.getMotDePasse(), candidat.getMotDePasse())) {
                return new AuthResponse(
                        jwtUtil.generateToken(candidat.getEmail(), "CANDIDAT"),
                        "CANDIDAT",
                        candidat.getId()
                );
            }
        }

        Optional<Recruteur> recruteurOpt = recruteurRepository.findByEmail(loginRequest.getEmail());
        if (recruteurOpt.isPresent()) {
            Recruteur recruteur = recruteurOpt.get();
            if (passwordEncoder.matches(loginRequest.getMotDePasse(), recruteur.getMotDePasse())) {
                return new AuthResponse(
                        jwtUtil.generateToken(recruteur.getEmail(), "RECRUTEUR"),
                        "RECRUTEUR",
                        recruteur.getId()
                );
            }
        }

        throw new AuthenticationException("Email ou mot de passe incorrect");
    }

    @Override
    public boolean isPasswordValid(String motDePasse) {
        return motDePasse != null && motDePasse.length() >= 6;
    }

    private void checkPasswordUniqueness(String encryptedPassword) {
        boolean passwordExists = candidatRepository.existsByMotDePasse(encryptedPassword) ||
                recruteurRepository.existsByMotDePasse(encryptedPassword);

        if (passwordExists) {
            throw new DuplicatePasswordException();
        }
    }

    @Override
    @Transactional
    public CandidatProfileResponse registerCandidat(CandidatRegisterRequest request) {
        if (candidatRepository.existsByEmail(request.getEmail()) ||
                recruteurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("L'email " + request.getEmail() + " est déjà utilisé");
        }
        //S'assurer que le mot de passe est unique
        String encryptedPassword = passwordEncoder.encode(request.getMotDePasse());
        checkPasswordUniqueness(encryptedPassword);

        Candidat candidat = new Candidat();
        candidat.setNom(request.getNom());
        candidat.setPrenom(request.getPrenom());
        candidat.setTelephone(request.getTelephone());
        candidat.setEmail(request.getEmail());
        candidat.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));

        Candidat savedCandidat = candidatRepository.save(candidat);
        return new CandidatProfileResponse(
                savedCandidat.getId(),
                savedCandidat.getNom(),
                savedCandidat.getPrenom(),
                savedCandidat.getEmail(),
                null,
                null,
                savedCandidat.getTelephone(),
                null,
                null,
                null
        );
    }
    @Override
    public CandidatProfileResponse updateCandidatProfile(
            Long id,
            String nom,
            String prenom,
            String telephone,
            String ville,
            String pays,
            String cvPath,
            List<String> certificationsPaths,
            String photoProfilPath
    ) {
        Candidat candidat = candidatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));
        if (nom != null) candidat.setNom(nom);
        if (prenom != null) candidat.setPrenom(prenom);
        if (telephone != null) candidat.setTelephone(telephone);
        if (ville != null) candidat.setVille(ville);
        if (pays != null) candidat.setPays(pays);
        if (cvPath != null) candidat.setCvPath(cvPath);
        if (certificationsPaths != null) {
            // 1. Récupérer les certifications existantes
            List<Certification> existingCerts = candidat.getCertifications();

            // 2. Identifier les certifications à supprimer (celles qui ne sont plus dans la liste)
            List<Certification> certsToRemove = existingCerts.stream()
                    .filter(existingCert -> !certificationsPaths.contains(existingCert.getPath()))
                    .collect(Collectors.toList());

            // 3. Supprimer les certifications
            certsToRemove.forEach(cert -> {
                try {
                    fileStorageService.deleteFile(cert.getPath()); // Supprime le fichier physique
                    existingCerts.remove(cert); // Supprime de la liste
                } catch (IOException e) {
                    logger.error("Erreur lors de la suppression du fichier de certification: " + cert.getPath(), e);
                }
            });

            // 4. Ajouter les nouvelles certifications
            certificationsPaths.forEach(path -> {
                if (!existingCerts.stream().anyMatch(c -> c.getPath().equals(path))) {
                    Certification cert = new Certification();
                    cert.setPath(path);
                    cert.setNom(extractFileNameFromPath(path));
                    cert.setCandidat(candidat);
                    existingCerts.add(cert);
                }
            });
        }
        if (photoProfilPath != null) candidat.setPhotoProfilPath(photoProfilPath);

        Candidat updated = candidatRepository.save(candidat);
        return new CandidatProfileResponse(
                updated.getId(),
                updated.getNom(),
                updated.getPrenom(),
                updated.getEmail(),
                updated.getVille(),
                updated.getPays(),
                updated.getTelephone(),
                updated.getCertifications(),
                updated.getCvPath(),
                updated.getPhotoProfilPath()
        );
    }

    private String extractFileNameFromPath(String path) {
        if (path == null || path.isEmpty()) {
            return "certification";
        }
        return path.substring(path.lastIndexOf('/') + 1);
    }

    @Override
    @Transactional
    public RecruteurProfileResponse registerRecruteur(RecruteurRegisterRequest request) {
        if (recruteurRepository.existsByEmail(request.getEmail()) ||
                candidatRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("L'email " + request.getEmail() + " est déjà utilisé");
        }
        Entreprise entreprise = entrepriseService.findOrCreate(request.getEntreprise());

        //S'assurer que le mot de passe est unique
        String encryptedPassword = passwordEncoder.encode(request.getMotDePasse());
        checkPasswordUniqueness(encryptedPassword);

        Recruteur recruteur = new Recruteur();
        recruteur.setNom(request.getNom());
        recruteur.setPrenom(request.getPrenom());
        recruteur.setEntreprise(entreprise);
        recruteur.setEmail(request.getEmail());
        recruteur.setPosition(request.getPosition());
        recruteur.setTelephone(request.getTelephone());
        recruteur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));

        Recruteur savedRecruteur = recruteurRepository.save(recruteur);
        return new RecruteurProfileResponse(
                savedRecruteur.getId(),
                savedRecruteur.getNom(),
                savedRecruteur.getPrenom(),
                savedRecruteur.getEmail(),
                savedRecruteur.getEntreprise() != null ? savedRecruteur.getEntreprise().getNom() : null,
                savedRecruteur.getPosition(),
                savedRecruteur.getTelephone(),
                null
        );
    }

    @Override
    public RecruteurProfileResponse updateRecruteurProfile(
            Long id,
            String nom,
            String prenom,
            String entrepriseNom,
            String position,
            String telephone,
            String photoProfilPath) {
        Recruteur recruteur = recruteurRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

        if (nom != null) recruteur.setNom(nom);
        if (prenom != null) recruteur.setPrenom(prenom);
        if (entrepriseNom != null) {
            Entreprise entreprise = entrepriseService.findOrCreate(entrepriseNom);
            recruteur.setEntreprise(entreprise); }
        if (position != null) recruteur.setPosition(position);
        if (telephone != null) recruteur.setTelephone(telephone);
        if (photoProfilPath != null) recruteur.setPhotoProfilPath(photoProfilPath);

        Recruteur updated = recruteurRepository.save(recruteur);
        return new RecruteurProfileResponse(
                updated.getId(),
                updated.getNom(),
                updated.getPrenom(),
                updated.getEmail(),
                updated.getEntreprise() != null ? updated.getEntreprise().getNom() : null,
                updated.getPosition(),
                updated.getTelephone(),
                updated.getPhotoProfilPath());
    }
}