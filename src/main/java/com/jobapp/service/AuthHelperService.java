package com.jobapp.service;

import com.jobapp.dto.response.UserIdentityResponse;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.RecruteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthHelperService {
    private final CandidatRepository candidatRepository;
    private final RecruteurRepository recruteurRepository;

    @Autowired
    public AuthHelperService(CandidatRepository candidatRepository,
                             RecruteurRepository recruteurRepository) {
        this.candidatRepository = candidatRepository;
        this.recruteurRepository = recruteurRepository;
    }

    public UserIdentityResponse checkUserExists(String email) {
        boolean emailExists = false;
        String role = null;

        // Vérification dans l'ordre candidat puis recruteur
        if (candidatRepository.existsByEmail(email)) {
            emailExists = true;
            role = "CANDIDAT";
        } else if (recruteurRepository.existsByEmail(email)) {
            emailExists = true;
            role = "RECRUTEUR";
        }

        return new UserIdentityResponse(emailExists, role);
    }
}
