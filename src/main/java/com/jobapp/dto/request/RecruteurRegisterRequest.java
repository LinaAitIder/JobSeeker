package com.jobapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RecruteurRegisterRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'entreprise est obligatoire")
    private String entrepriseNom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Votre position dans l'entreprise est obligatoire")
    private String position;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEntrepriseNom() { return entrepriseNom; }

    public void setEntrepriseNom(String entrepriseNom) {
        this.entrepriseNom = entrepriseNom; }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}