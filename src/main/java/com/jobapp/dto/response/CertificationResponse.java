package com.jobapp.dto.response;

import com.jobapp.model.Certification;

public record CertificationResponse(
        Long id,
        String nom,
        String fileUrl
) {
    public CertificationResponse(Certification certification) {
        this(
                certification.getId(),
                certification.getNom(),
                certification.getPath()
        );
    }
}