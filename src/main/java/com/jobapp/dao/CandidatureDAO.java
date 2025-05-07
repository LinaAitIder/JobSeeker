package com.jobapp.dao;

import com.jobapp.model.Candidature;
import com.jobapp.model.OffreEmploi;
import com.jobapp.model.Candidat;
import com.jobapp.util.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatureDAO {

    // Créer une candidature
    public boolean createCandidature(Candidature candidature) {
        String sql = "INSERT INTO Candidature (candidat_id, offre_id, date_postulation, lettre_motivation_path, message_recruteur, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, candidature.getCandidat().getId());
            stmt.setLong(2, candidature.getOffre().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(candidature.getDatePostulation()));
            stmt.setString(4, candidature.getLettreMotivationPath());
            stmt.setString(5, candidature.getMessageRecruteur());
            if (candidature.getStatut() == null) {
                candidature.setStatut(Candidature.Statut.EN_ATTENTE);
            }
            stmt.setString(6, candidature.getStatut().name());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        candidature.setId(rs.getLong(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Récupérer les candidatures par offre
    public List<Candidature> getCandidaturesByOffre(Long offreId) {
        List<Candidature> candidatures = new ArrayList<>();
        String sql = "SELECT c.* FROM Candidature c WHERE c.offre_id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, offreId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                candidatures.add(mapResultSetToCandidature(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidatures;
    }

    // Mapper ResultSet -> Candidature
    private Candidature mapResultSetToCandidature(ResultSet rs) throws SQLException {
        Candidature candidature = new Candidature();
        candidature.setId(rs.getLong("id"));

        // On initialise seulement les IDs pour éviter de charger les objets complets
        Candidat candidat = new Candidat();
        candidat.setId(rs.getLong("candidat_id"));
        candidature.setCandidat(candidat);

        OffreEmploi offre = new OffreEmploi();
        offre.setId(rs.getLong("offre_id"));
        candidature.setOffre(offre);

        candidature.setDatePostulation(rs.getTimestamp("date_postulation").toLocalDateTime());
        candidature.setLettreMotivationPath(rs.getString("lettre_motivation_path"));
        candidature.setMessageRecruteur(rs.getString("message_recruteur"));
        candidature.setStatut(Candidature.Statut.valueOf(rs.getString("statut")));

        return candidature;
    }

    // Mettre à jour le statut d'une candidature
    public boolean updateStatut(Long candidatureId, Candidature.Statut newStatut) {
        String sql = "UPDATE Candidature SET statut = ? WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatut.name());
            stmt.setLong(2, candidatureId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}