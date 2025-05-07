package com.jobapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidature")
public class Candidature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidat_id", columnDefinition = "BIGINT")
    private Candidat candidat;

    @ManyToOne
    @JoinColumn(name = "offre_id", columnDefinition = "BIGINT")
    private OffreEmploi offre;

    @Column(name = "date_postulation")
    private LocalDateTime datePostulation;

    @Column(name = "lettre_motivation_path")
    private String lettreMotivationPath;

    @Column(name = "message_recruteur")
    private String messageRecruteur;

    public enum Statut {
        EN_ATTENTE,
        ACCEPTEE,
        REFUSEE
    }

    @Enumerated(EnumType.STRING)
    private Statut statut;

    public Candidature() {}

    public Candidature(Candidat candidat, OffreEmploi offre, String lettreMotivationPath, String messageRecruteur, Statut statut) {
        this.candidat = candidat;
        this.offre = offre;
        this.lettreMotivationPath = lettreMotivationPath;
        this.messageRecruteur = messageRecruteur;
        this.statut = statut;
        this.datePostulation = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public OffreEmploi getOffre() {
        return offre;
    }

    public void setOffre(OffreEmploi offre) {
        this.offre = offre;
    }

    public LocalDateTime getDatePostulation() {
        return datePostulation;
    }

    public void setDatePostulation(LocalDateTime datePostulation) {
        this.datePostulation = datePostulation;
    }

    public String getLettreMotivationPath() {
        return lettreMotivationPath;
    }

    public void setLettreMotivationPath(String lettreMotivation) {
        this.lettreMotivationPath = lettreMotivationPath;
    }

    public String getMessageRecruteur() {
        return messageRecruteur;
    }

    public void setMessageRecruteur(String messageRecruteur) {
        this.messageRecruteur = messageRecruteur;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
}