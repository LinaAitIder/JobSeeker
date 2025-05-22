package com.jobapp.repository;

import com.jobapp.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntrepriseDAO extends JpaRepository<Entreprise, Long> {

    @Query("SELECT e FROM Entreprise e WHERE e.nom = :nom")
    Optional<Entreprise> findByNom(@Param("nom") String nom);

    @Query("SELECT e FROM Entreprise e WHERE e.nom LIKE %:nom%")
    Optional<Entreprise> findByNomContaining(@Param("nom") String nom);

    List<Entreprise> findByDomaine(String domaine);


}
