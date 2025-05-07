package com.jobapp.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "candidat")
public class Candidat implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    private String nom;
    private String prenom;
    private String telephone;
    private String email;

    //Unconsistent naming in the backe

    @Column(name = "mot_de_passe")
    private String motDePasse;

    private String ville;
    private String pays;

    @Column(name = "cv_path")
    private String cvPath;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications = new ArrayList<>();

    @Column(name = "photo_profil_path")
    private String photoProfilPath;

    public Candidat() {}

    // Constructeur minimal (pour l'inscription)
    public Candidat(String nom, String prenom, String email, String motDePasse, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
    }

    // Constructeur complet (pour tests et mises a jour)
    public Candidat(String nom, String prenom, String ville, String pays,
                    String telephone, String email, String motDePasse,
                    String cvPath, List<Certification> certifications, String photoProfilPath) {
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.pays = pays;
        this.telephone = telephone;
        this.email = email;
        this.motDePasse = motDePasse;
        this.cvPath = cvPath;
        this.certifications = certifications;
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String MotDePasse) {
        this.motDePasse = MotDePasse;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public List<Certification> getCertifications() { return certifications; }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications; }

    public String getPhotoProfilPath() { return photoProfilPath; }

    public void setPhotoProfilPath(String photoProfilPath) { this.photoProfilPath = photoProfilPath; }
}