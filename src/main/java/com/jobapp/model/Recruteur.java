package com.jobapp.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "recruteur")
public class Recruteur implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    private String nom;
    private String prenom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    private String email;
    private String position;
    private String telephone;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "photo_profil_path")
    private String photoProfilPath;

    public Recruteur() {}

    // Constructeur sans photo profil pour l'inscription
    public Recruteur(String nom, String prenom, Entreprise entreprise, String email,
                     String position, String telephone, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.entreprise = entreprise;
        this.email = email;
        this.position = position;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
    }
    // Constructeur complet pur tests et mises a jour
    public Recruteur(String nom, String prenom, Entreprise entreprise, String email,
                     String position, String telephone, String motDePasse, String photoProfilPath) {
        this.nom = nom;
        this.prenom = prenom;
        this.entreprise = entreprise;
        this.email = email;
        this.position = position;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
        this.photoProfilPath = photoProfilPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getPhotoProfilPath() { return photoProfilPath; }

    public void setPhotoProfilPath(String photoProfilPath) { this.photoProfilPath = photoProfilPath; }
}

