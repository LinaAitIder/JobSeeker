package com.jobapp.dao;

import com.jobapp.model.Candidat;
import com.jobapp.model.Certification;
import com.jobapp.util.MySQLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CandidatDAO {
    // ENREGISTRE UN NOUVEAU CANDIDAT
    public boolean register(Candidat candidat) {
        String sql = "INSERT INTO Candidat (nom, prenom, email, mot_de_passe, telephone) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, candidat.getNom());
            stmt.setString(2, candidat.getPrenom());
            stmt.setString(3, candidat.getEmail());
            stmt.setString(4, candidat.getMotDePasse());
            stmt.setString(5, candidat.getTelephone());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        candidat.setId(generatedKeys.getLong(1));
                    }
                }
            }

            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMotDePasse(Long id, String nouveauMotDePasse) {
        String sql = "UPDATE Candidat SET mot_de_passe = ? WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nouveauMotDePasse);
            stmt.setLong(2, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addCertification(Long candidatId, String path, String nom) {
        String sql = "INSERT INTO Certification (path, nom, candidat_id) VALUES (?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, path);
            stmt.setString(2, nom);
            stmt.setLong(3, candidatId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Candidat login(String email, String motDePasse) {
        String sql = "SELECT c.id, c.nom, c.prenom, c.telephone, c.email, " +
                "c.mot_de_passe, c.ville, c.pays, c.cv_path, c.photo_profil_path " +
                "FROM Candidat c WHERE c.email = ? AND c.mot_de_passe = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, motDePasse);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Candidat cand = new Candidat();
                cand.setId(rs.getLong("id"));
                cand.setNom(rs.getString("nom"));
                cand.setPrenom(rs.getString("prenom"));
                cand.setTelephone(rs.getString("telephone"));
                cand.setEmail(rs.getString("email"));
                cand.setMotDePasse(rs.getString("mot_de_passe"));
                cand.setVille(rs.getString("ville"));
                cand.setPays(rs.getString("pays"));
                cand.setCvPath(rs.getString("cv_path"));
                cand.setPhotoProfilPath(rs.getString("photo_profil_path"));

                List<Certification> certifications = getCertificationsForCandidat(cand.getId(), conn);
                cand.setCertifications(certifications);

                return cand;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Certification> getCertificationsForCandidat(Long candidatId, Connection conn) throws SQLException {
        List<Certification> certifications = new ArrayList<>();
        String sql = "SELECT id, path, nom FROM Certification WHERE candidat_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, candidatId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Certification cert = new Certification();
                cert.setId(rs.getLong("id"));
                cert.setPath(rs.getString("path"));
                cert.setNom(rs.getString("nom"));
                certifications.add(cert);
            }
        }
        return certifications;
    }

    public boolean deleteCandidat(Long id) {
        String sql = "DELETE FROM Candidat WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

