package com.jobapp.dto.response;

import org.springframework.lang.Nullable;

public record RecruteurProfileResponse(
        Long id,
        String nom,
        String prenom,
        String email,
        String entrepriseNom,
        String position,
        String telephone,
        @Nullable String photoProfilPath
) {}