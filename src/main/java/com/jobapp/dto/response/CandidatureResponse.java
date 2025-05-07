package com.jobapp.dto.response;

import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public class CandidatureResponse {
    private final Long id;
    private final Long candidatId;
    private final Long offreId;
    private final LocalDateTime datePostulation;
    @Nullable private final String lettreMotivationPath;
    @Nullable private final String messageRecruteur;
    private final String statut;

    public CandidatureResponse(Long id, Long candidatId, Long offreId,
                               LocalDateTime datePostulation, String lettreMotivationPath,
                               String messageRecruteur, String statut) {
        this.id = id;
        this.candidatId = candidatId;
        this.offreId = offreId;
        this.datePostulation = datePostulation;
        this.lettreMotivationPath = lettreMotivationPath;
        this.messageRecruteur = messageRecruteur;
        this.statut = statut;
    }

    public Long getId() { return id; }
    public Long getCandidatId() { return candidatId; }
    public Long getOffreId() { return offreId; }
    public LocalDateTime getDatePostulation() { return datePostulation; }
    public String getLettreMotivationPath() { return lettreMotivationPath; }
    public String getMessageRecruteur() { return messageRecruteur; }
    public String getStatut() { return statut; }
}