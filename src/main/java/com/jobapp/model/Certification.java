package com.jobapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "certification")
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;
    private String nom;


    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false, columnDefinition = "BIGINT")
    private Candidat candidat;

    public Certification() {}

    public Certification(Long id, String path, String nom, Candidat candidat) {
        this.id = id;
        this.path = path;
        this.nom = nom;
        this.candidat = candidat;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public Candidat getCandidat() { return candidat; }

    public void setCandidat(Candidat candidat) { this.candidat = candidat; }
}