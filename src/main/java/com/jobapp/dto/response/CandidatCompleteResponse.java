package com.jobapp.dto.response;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.util.List;

public class CandidatCompleteResponse {
    private final Long id;
    private final String nom;
    private final String prenom;
    private final String email;
    @Nullable private final String ville;
    @Nullable private final String pays;
    private final String telephone;
    @Nullable private final Resource photoProfil;
    @Nullable private final Resource cv;
    @Nullable private final List<CertificationFileResponse> certifications;

    public CandidatCompleteResponse(Long id, String nom, String prenom, String email,
                                    String ville, String pays, String telephone,
                                    Resource photoProfil, Resource cv,
                                    List<CertificationFileResponse> certifications) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.ville = ville;
        this.pays = pays;
        this.telephone = telephone;
        this.photoProfil = photoProfil;
        this.cv = cv;
        this.certifications = certifications;
    }

    public Long getId() {
        return id;
    }


    public String getNom() {
        return nom;
    }


    public String getPrenom() {
        return prenom;
    }


    public String getEmail() {
        return email;
    }


    public String getVille() {
        return ville;
    }

    public String getPays() {
        return pays;
    }


    public String getTelephone() {
        return telephone;
    }


    public Resource getPhotoProfil() {
        return photoProfil;
    }


    public Resource getCv() {
        return cv;
    }


    public List<CertificationFileResponse> getCertifications() {
        return certifications;
    }

}
