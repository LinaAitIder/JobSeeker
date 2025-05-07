package com.jobapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "offre_emploi")
public class OffreEmploi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    private String titre;
    private String description;
    private String domaine;
    private String ville;
    private String pays;

    @ManyToOne
    @JoinColumn(name = "recruteurId", nullable = false, columnDefinition = "BIGINT")
    private Recruteur recruteur;

    private LocalDate datePublication;
    private LocalDate dateExpiration;
    private Double salaireMin;
    private Double salaireMax;
    private String typeContrat;

    // Constructeurs
    public OffreEmploi() {}

    public OffreEmploi(String titre, String description, String domaine,
                       String ville, String pays, Recruteur recruteur) {
        this.titre = titre;
        this.description = description;
        this.domaine = domaine;
        this.ville = ville;
        this.pays = pays;
        this.recruteur = recruteur;
    }

    public Long getId() { return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
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

    public Recruteur getRecruteur() {
        return recruteur;
    }

    public void setRecruteur(Recruteur recruteur) {
        this.recruteur = recruteur;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Double getSalaireMin() {
        return salaireMin;
    }

    public void setSalaireMin(Double salaireMin) {
        this.salaireMin = salaireMin;
    }

    public Double getSalaireMax() {
        return salaireMax;
    }

    public void setSalaireMax(Double salaireMax) {
        this.salaireMax = salaireMax;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

}