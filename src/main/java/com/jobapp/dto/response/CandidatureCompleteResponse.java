package com.jobapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public class CandidatureCompleteResponse {
    @JsonProperty("id")
    private final Long id;
    private final Long candidatId;
    private final String candidatNom;
    private final String candidatPrenom;
    private final Long offreId;
    private final String offreTitre;
    private final LocalDateTime datePostulation;
    @Nullable private final Resource lettreMotivation;
    @Nullable private final String messageRecruteur;
    private final String statut;

    public CandidatureCompleteResponse(Long id, Long candidatId, String candidatNom,
                                       String candidatPrenom,Long offreId, String offreTitre,
                                       LocalDateTime datePostulation, Resource lettreMotivation,
                                       String messageRecruteur, String statut) {
        this.id = id;
        this.candidatId = candidatId;
        this.candidatNom = candidatNom;
        this.candidatPrenom = candidatPrenom;
        this.offreId = offreId;
        this.offreTitre = offreTitre;
        this.datePostulation = datePostulation;
        this.lettreMotivation = lettreMotivation;
        this.messageRecruteur = messageRecruteur;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public Long getCandidatId() {
        return candidatId;
    }

    public String getCandidatNom() {
        return candidatNom;
    }

    public String getCandidatPrenom() {
        return candidatPrenom;
    }

    public Long getOffreId() {
        return offreId;
    }

    public String getOffreTitre() {
        return offreTitre;
    }

    public LocalDateTime getDatePostulation() {
        return datePostulation;
    }

    @Nullable
    public Resource getLettreMotivation() {
        return lettreMotivation;
    }

    @Nullable
    public String getMessageRecruteur() {
        return messageRecruteur;
    }

    public String getStatut() {
        return statut;
    }
}
