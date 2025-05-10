package com.jobapp.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class UpdateRecruteurProfileRequest {
    private String nom;
    private String prenom;
    private String entrepriseNom;
    private String position;
    private String telephone;
    private MultipartFile photoProfilFile;

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEntrepriseNom() { return entrepriseNom; }

    public String getPosition() { return position; }

    public void setPosition(String position) { this.position = position; }

    public void setEntrepriseNom(String entrepriseNom) { this.entrepriseNom = entrepriseNom; }

    public String getTelephone() { return telephone; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public MultipartFile getPhotoProfilFile() { return photoProfilFile; }

    public void setPhotoProfilFile(MultipartFile photoProfilFile) { this.photoProfilFile = photoProfilFile; }
}