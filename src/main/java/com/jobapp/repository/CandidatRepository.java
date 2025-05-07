package com.jobapp.repository;

import com.jobapp.model.Candidat;
import com.jobapp.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {

    // Trouver un candidat par email
    Optional<Candidat> findByEmail(String email);

    // Verifier si un email existe
    boolean existsByEmail(String email);

    // Verifier si un mot de passe existe
    boolean existsByMotDePasse(String encryptedPassword);

    @Modifying
    @Query("UPDATE Candidat c SET c.photoProfilPath = :filename WHERE c.id = :id")
    void updatePhotoProfil(@Param("id") Long id, @Param("filename") String filename);

    @Modifying
    @Query("UPDATE Candidat c SET c.cvPath = :cvPath WHERE c.id = :id")
    void updateCvPath(@Param("id") Long id, @Param("cvPath") String cvPath);

    @Query("SELECT c FROM Certification c WHERE c.candidat.id = :candidatId")
    List<Certification> findCertificationsByCandidatId(@Param("candidatId") Long candidatId);

    @Modifying
    @Query("UPDATE Candidat c SET " +
            "c.nom = :nom, " +
            "c.prenom = :prenom, " +
            "c.ville = :ville, " +
            "c.pays = :pays, " +
            "c.telephone = :telephone " +
            "WHERE c.id = :id")
    void updateProfilBasique(
            @Param("id") Long id,
            @Param("nom") String nom,
            @Param("prenom") String prenom,
            @Param("ville") String ville,
            @Param("pays") String pays,
            @Param("telephone") String telephone);
    }

    // Methodes heritees automatiquement de JpaRepository:
    // - save(S entity)
    // - findById(ID id)
    // - findAll()
    // - deleteById(ID id)
    // etc...........
