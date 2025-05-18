package com.jobapp.repository;

import com.jobapp.model.Candidature;
import com.jobapp.model.OffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    // Trouver les candidatures par offre
    List<Candidature> findByOffreId(Long offreId);

    @Modifying
    @Query("DELETE FROM Candidature c WHERE c.offre.id = :offreId")
    void deleteByOffreId(@Param("offreId") Long offreId);

    // Trouver les candidatures par candidat
    List<Candidature> findByCandidatId(Long candidatId);

    // Verifier si un candidat a déjà postulé a une offre
    boolean existsByCandidatIdAndOffreId(Long candidatId, Long offreId);

    List<Candidature> findByOffreIdAndStatut(Long offreId, Candidature.Statut statut);

    @Modifying
    @Query("DELETE FROM Candidature c WHERE c.candidat.id = :candidatId")
    void deleteByCandidatId(@Param("candidatId") Long candidatId);

}