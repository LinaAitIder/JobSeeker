package com.jobapp.service;

import com.jobapp.model.OffreEmploi;

import java.util.List;

public interface IARecommendationService {
    // return Liste des IDs d'offres triées
    public List<Long> getRankedOffers(String cvText, List<String> offersAsText,List<OffreEmploi> activeOffers);
}