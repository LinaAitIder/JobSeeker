package com.jobapp.dto.response;

import com.jobapp.model.Candidature;

import java.time.LocalDateTime;

public class CandidatureDTO {
    private Long id;
    private Long offreId;
    private Candidature.Statut statut;
    private LocalDateTime datePostulation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOffreId() {
        return offreId;
    }

    public void setOffreId(Long offreId) {
        this.offreId = offreId;
    }

    public Candidature.Statut getStatut() {
        return statut;
    }

    public void setStatut(Candidature.Statut statut) {
        this.statut = statut;
    }

    public LocalDateTime getDatePostulation() {
        return datePostulation;
    }

    public void setDatePostulation(LocalDateTime datePostulation) {
        this.datePostulation = datePostulation;
    }
}