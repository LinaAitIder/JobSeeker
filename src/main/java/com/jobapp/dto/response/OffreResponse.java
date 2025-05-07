package com.jobapp.dto.response;

import java.time.LocalDate;

public class OffreResponse {
    private final Long id;
    private final String titre;
    private final String description;
    private final String domaine;
    private final String ville;
    private final String pays;
    private final LocalDate datePublication;
    private final LocalDate dateExpiration;
    private final Double salaireMin;
    private final Double salaireMax;
    private final String typeContrat;
    private final Long recruteurId;

    // Constructeur simplifié
    public OffreResponse(Long id, String titre, String description, String domaine,
                         String ville, String pays, Long recruteurId,
                         LocalDate datePublication, LocalDate dateExpiration,
                         Double salaireMin, Double salaireMax, String typeContrat) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.domaine = domaine;
        this.ville = ville;
        this.pays = pays;
        this.recruteurId = recruteurId;
        this.datePublication = datePublication;
        this.dateExpiration = dateExpiration;
        this.salaireMin = salaireMin;
        this.salaireMax = salaireMax;
        this.typeContrat = typeContrat;
    }

    // Getters uniquement
    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getDomaine() { return domaine; }
    public String getVille() { return ville; }
    public String getPays() { return pays; }
    public LocalDate getDatePublication() { return datePublication; }
    public LocalDate getDateExpiration() { return dateExpiration; }
    public Double getSalaireMin() { return salaireMin; }
    public Double getSalaireMax() { return salaireMax; }
    public String getTypeContrat() { return typeContrat; }
    public Long getRecruteurId() { return recruteurId; }
}
