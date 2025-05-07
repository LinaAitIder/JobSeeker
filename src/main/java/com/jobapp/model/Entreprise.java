package com.jobapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "entreprise")
public class Entreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private String location;

    @Enumerated(EnumType.STRING)
    private TailleEntreprise taille;

    private String domaine;

    @Column(name = "logo_path")
    private String logoPath;

    public enum TailleEntreprise {
        PETITE(50, 250),
        MOYENNE(251, 1000),
        GRANDE(1001, Integer.MAX_VALUE);

        private final int minEmployes;
        private final int maxEmployes;

        TailleEntreprise(int min, int max) {
            this.minEmployes = min;
            this.maxEmployes = max;
        }
    }
    public Entreprise() {}

    public Entreprise(String nom, String description, String location,
                      TailleEntreprise taille, String domaine) {
        this.nom = nom;
        this.description = description;
        this.location = location;
        this.taille = taille;
        this.domaine = domaine;
    }

    public Entreprise(String nom, String description, String location,
                      TailleEntreprise taille, String domaine, String logoPath) {
        this.nom = nom;
        this.description = description;
        this.location = location;
        this.taille = taille;
        this.domaine = domaine;
        this.logoPath = logoPath;

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public String getLogoPath() { return logoPath; }

    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }
}