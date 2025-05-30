package com.jobapp.dto.response;

import org.springframework.core.io.Resource;

public class CertificationFileResponse {
    private final Long id;
    private final String nom;
    private final Resource file;


    public CertificationFileResponse(Long id, String nom, Resource file) {
        this.id = id;
        this.nom = nom;
        this.file = file;
    }

    public Long getId() {
        return id;
    }


    public String getNom() {
        return nom;
    }


    public Resource getFile() {
        return file;
    }

}