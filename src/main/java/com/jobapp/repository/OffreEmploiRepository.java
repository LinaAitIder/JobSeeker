package com.jobapp.repository;

import com.jobapp.model.OffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Long> {

    // Trouver les offres par recruteur
    List<OffreEmploi> findByRecruteurId(Long recruteurId);

    // Trouver les offres par entreprise
    List<OffreEmploi> findByRecruteurEntrepriseId(Long entrepriseId);

    @Modifying
    @Query("DELETE FROM OffreEmploi o WHERE o.recruteur.id = :recruteurId")
    void deleteByRecruteurId(@Param("recruteurId") Long recruteurId);

    // Recherche d'offres
    @Query("SELECT o FROM OffreEmploi o WHERE LOWER(o.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(o.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<OffreEmploi> findByTitreContainingOrDescriptionContaining(@Param("searchTerm") String searchTerm);
    // Trouver les offres par domaine
    List<OffreEmploi> findByDomaine(String domaine);

    // Trouver les offres par localisation
    List<OffreEmploi> findByVilleAndPays(String ville, String pays);
}