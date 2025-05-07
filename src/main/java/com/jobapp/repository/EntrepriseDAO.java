package com.jobapp.repository;

import com.jobapp.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntrepriseDAO extends JpaRepository<Entreprise, Long> {
    Optional<Entreprise> findByNom(String nom);
    List<Entreprise> findByDomaine(String domaine);
}
