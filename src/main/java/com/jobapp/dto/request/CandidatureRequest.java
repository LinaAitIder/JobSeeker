package com.jobapp.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class CandidatureRequest {
    private Long offreId;
    private MultipartFile lettreMotivationFile;
    private String messageRecruteur;

    public Long getOffreId() {
        return offreId;
    }

    public void setOffreId(Long offreId) {
        this.offreId = offreId;
    }

    public MultipartFile getLettreMotivationFile() {
        return lettreMotivationFile;
    }

    public void setLettreMotivationFile(MultipartFile lettreMotivationFile) {
        this.lettreMotivationFile = lettreMotivationFile; }

    public String getMessageRecruteur() {
        return messageRecruteur;
    }

    public void setMessageRecruteur(String messageRecruteur) {
        this.messageRecruteur = messageRecruteur;
    }
}