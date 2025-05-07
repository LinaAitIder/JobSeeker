package com.jobapp.dto.request;

public class CertificationRequest {
    private String nom;
    private String path;

    public CertificationRequest() {}

    public CertificationRequest(String nom, String path) {
        this.nom = nom;
        this.path = path;
    }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }
}
