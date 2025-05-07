package com.jobapp.service;

import com.jobapp.dto.exception.AuthenticationException;
import com.jobapp.dto.request.*;
import com.jobapp.dto.response.*;
import com.jobapp.model.Certification;

import java.util.List;

public interface AuthService {
    AuthResponse authenticate(LoginRequest loginRequest) throws AuthenticationException;
    boolean isPasswordValid(String password);
    CandidatProfileResponse registerCandidat(CandidatRegisterRequest request);
    RecruteurProfileResponse registerRecruteur(RecruteurRegisterRequest request);
    CandidatProfileResponse updateCandidatProfile(
            Long id,
            String nom,
            String prenom,
            String telephone,
            String ville,
            String pays,
            String cvPath,
            List<String> certificationsPaths,
            String photoProfilPath
    );
    RecruteurProfileResponse updateRecruteurProfile(
            Long id,
            String nom,
            String prenom,
            String entrepriseNom,
            String position,
            String telephone,
            String photoProfilPath
    );

}