package com.jobapp.dto.response;


import com.jobapp.model.Certification;
import org.springframework.lang.Nullable;

import java.util.List;

public record CandidatProfileResponse(
        Long id,
        String nom,
        String prenom,
        String email,
        @Nullable String ville,
        @Nullable String pays,
        String telephone,
        @Nullable List<Certification> certificationsPaths,
        @Nullable String cvPath,
        @Nullable String photoProfilPath
) {}