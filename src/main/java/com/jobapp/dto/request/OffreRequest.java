package com.jobapp.dto.request;


import java.time.LocalDate;

public class OffreRequest {
    private String titre;
    private String description;
    private String domaine;
    private String ville;
    private String pays;
    private Double salaireMin;
    private Double salaireMax;
    private String typeContrat;
    private LocalDate datePublication;
    private LocalDate dateExpiration;
    private Long recruteurId;

    // Getters & Setters
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

    public Double getSalaireMin() {
        return salaireMin;
    }

    public void setSalaireMin(Double salaireMin) {
        this.salaireMin = salaireMin;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public Double getSalaireMax() {
        return salaireMax;
    }

    public void setSalaireMax(Double salaireMax) {
        this.salaireMax = salaireMax;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public Long getRecruteurId() {
        return recruteurId;
    }

    public void setRecruteurId(Long recruteurId) {
        this.recruteurId = recruteurId;
    }
}
