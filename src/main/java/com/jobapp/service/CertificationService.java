package com.jobapp.service;

import com.jobapp.model.Certification;

import java.util.List;

public interface CertificationService {
    Certification addCertification(Long candidatId, String filePath, String nom);
    void removeCertification(Long certificationId);
    Certification getCertification(Long certificationId);
    List<Certification> getCertificationsByCandidatId(Long candidatId);
    void deleteAllByCandidatId(Long candidatId);
}
