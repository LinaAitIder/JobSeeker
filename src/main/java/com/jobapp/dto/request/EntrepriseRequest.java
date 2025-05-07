package com.jobapp.dto.request;

import com.jobapp.model.Entreprise;
import com.jobapp.model.Entreprise.TailleEntreprise;
import jakarta.validation.constraints.NotBlank;

public class EntrepriseRequest {
    @NotBlank
    private String nom;

    private String description;
    private String location;

    private TailleEntreprise taille; // "PETITE", "MOYENNE", "GRANDE"

    private String domaine;


    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public TailleEntreprise getTaille() { return taille; }

    public void setTaille(TailleEntreprise taille) { this.taille = taille; }

    public String getDomaine() { return domaine; }

    public void setDomaine(String domaine) { this.domaine = domaine; }
}
