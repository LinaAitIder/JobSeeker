package com.jobapp.service.impl;

import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.model.Candidat;
import com.jobapp.model.Certification;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.CertificationRepository;
import com.jobapp.service.CertificationService;
import com.jobapp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CertificationServiceImpl implements CertificationService {

    private CertificationRepository certificationRepository;

    private CandidatRepository candidatRepository;

    private FileStorageService fileStorageService;

    @Override
    public Certification addCertification(Long candidatId, String filePath, String nom) {
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

        Certification certification = new Certification();
        certification.setPath(filePath);
        certification.setNom(nom);
        certification.setCandidat(candidat);

        return certificationRepository.save(certification);
    }

    @Override
    public void removeCertification(Long certificationId) {
        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException("Certification non trouvée"));

        try {
            fileStorageService.deleteFile(certification.getPath());
        } catch (IOException e) {
            System.err.println("Failed to delete certification file: " + certification.getPath());
        }

        certificationRepository.delete(certification);
    }

    @Override
    public Certification getCertification(Long certificationId) {
        return certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException("Certification non trouvée"));
    }

    @Override
    public List<Certification> getCertificationsByCandidatId(Long candidatId) {
        return certificationRepository.findByCandidatId(candidatId);
    }

    @Override
    public void deleteAllByCandidatId(Long candidatId) {
        // Récupère toutes les certifications du candidat
        List<Certification> certifications = certificationRepository.findByCandidatId(candidatId);

        // Supprime les fichiers physiques puis les entités en base
        for (Certification certification : certifications) {
            try {
                if (certification.getPath() != null && !certification.getPath().isEmpty()) {
                    fileStorageService.deleteFile(certification.getPath());
                }
            } catch (IOException e) {
                // Log l'erreur mais continue la suppression des autres fichiers
                System.err.println("Erreur lors de la suppression du fichier de certification: "
                        + certification.getPath() + " - " + e.getMessage());
            }
        }

        // Suppression en masse plus efficace que deleteAll()
        certificationRepository.deleteByCandidatId(candidatId);
    }
}
