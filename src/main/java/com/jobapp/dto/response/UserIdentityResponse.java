package com.jobapp.dto.response;

public class UserIdentityResponse {
    private boolean emailExists;
    private String role; // "CANDIDAT" ou "RECRUTEUR" ou null

    public UserIdentityResponse(boolean emailExists, String role) {
        this.emailExists = emailExists;
        this.role = role;
    }

    public boolean isEmailExists() { return emailExists; }

    public String getRole() { return role; }
}
