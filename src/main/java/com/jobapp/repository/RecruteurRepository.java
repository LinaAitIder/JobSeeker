package com.jobapp.repository;

import com.jobapp.model.Recruteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruteurRepository extends JpaRepository<Recruteur, Long> {

    // Trouver un recruteur par email
    Optional<Recruteur> findByEmail(String email);

    // Verifier si un email existe
    boolean existsByEmail(String email);

    // Verifier si un mot de passe existe
    boolean existsByMotDePasse(String encryptedPassword);

    @Modifying
    @Query("DELETE FROM OffreEmploi o WHERE o.recruteur.id = :recruteurId")
    void deleteOffresByRecruteurId(@Param("recruteurId") Long recruteurId);

    @Modifying
    @Query("UPDATE Recruteur r SET r.photoProfilPath = :filename WHERE r.id = :id")
    void updatePhotoProfil(@Param("id") Long id, @Param("filename") String filename);

    @Modifying
    @Query("UPDATE Recruteur r SET " +
            "r.nom = :nom, " +
            "r.prenom = :prenom, " +
            "r.position = :position, " +
            "r.telephone = :telephone " +
            "WHERE r.id = :id")
    void updateProfilBasique(
            @Param("id") Long id,
            @Param("nom") String nom,
            @Param("prenom") String prenom,
            @Param("position") String position,
            @Param("telephone") String telephone);

    // Methode separee pour l'entreprise
    @Modifying
    @Query("UPDATE Recruteur r SET r.entreprise.id = :entrepriseId WHERE r.id = :id")
    void updateEntreprise(@Param("id") Long id, @Param("entrepriseId") Long entrepriseId);

    // Methodes heritees automatiquement:
    // save(), findById(), findAll(), etc........
}