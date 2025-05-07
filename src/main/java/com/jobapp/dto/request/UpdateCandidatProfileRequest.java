package com.jobapp.dto.request;

import com.jobapp.model.Certification;

import java.util.List;

public class UpdateCandidatProfileRequest {
    private String nom;
    private String prenom;
    private String ville;
    private String pays;
    private String telephone;;
    private String cvPath;
    private List<CertificationRequest> certifications;
    private String photoProfilPath;


    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getVille() { return ville; }

    public void setVille(String ville) { this.ville = ville; }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public List<CertificationRequest> getCertifications() { return certifications; }

    public void setCertifications(List<CertificationRequest> certifications) {
        this.certifications = certifications; }

    public String getPhotoProfilPath() {
        return photoProfilPath;
    }

    public void setPhotoProfilPath(String photoProfilPath) {
        this.photoProfilPath = photoProfilPath;
    }
}