package com.jobapp.repository;

import com.jobapp.model.Candidat;
import com.jobapp.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByCandidatId(Long candidatId);

    @Modifying
    @Query("DELETE FROM Certification c WHERE c.candidat.id = :candidatId AND c.path = :path")
    void deleteByCandidatIdAndPath(@Param("candidatId") Long candidatId, @Param("path") String path);

    @Query("SELECT c FROM Certification c WHERE c.candidat.id = :candidatId ORDER BY c.nom ASC")
    List<Certification> findCertificationsByCandidat(@Param("candidatId") Long candidatId);

    @Modifying
    @Query("INSERT INTO Certification (path, nom, candidat) VALUES (:path, :nom, :candidat)")
    void addCertification(
            @Param("path") String path,
            @Param("nom") String nom,
            @Param("candidat") Candidat candidat);

    @Modifying
    @Query("DELETE FROM Certification c WHERE c.candidat.id = :candidatId")
    void deleteByCandidatId(@Param("candidatId") Long candidatId);
}
