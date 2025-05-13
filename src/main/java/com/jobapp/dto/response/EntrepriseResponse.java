package com.jobapp.dto.response;

import com.jobapp.model.Entreprise;
import org.springframework.lang.Nullable;

public record EntrepriseResponse(
        Long id,
        String nom,
        @Nullable String description,
        @Nullable String location,
        @Nullable String taille,
        @Nullable String domaine,
        @Nullable String logoPath
) {
    public EntrepriseResponse(Entreprise entreprise) {
        this(
                entreprise.getId(),
                entreprise.getNom(),
                entreprise.getDescription(),
                entreprise.getLocation(),
                entreprise.getTaille().name(),
                entreprise.getDomaine(),
                entreprise.getLogoPath()
        );
    }
}
