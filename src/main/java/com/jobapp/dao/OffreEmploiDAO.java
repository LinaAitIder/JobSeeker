package com.jobapp.dao;

import com.jobapp.model.OffreEmploi;
import com.jobapp.model.Recruteur;
import com.jobapp.util.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreEmploiDAO {

    // Créer une offre d'emploi
    public boolean createOffre(OffreEmploi offre) {
        String sql = "INSERT INTO OffreEmploi (titre, description, domaine, ville, pays, recruteurId, " +
                "datePublication, dateExpiration, salaireMin, salaireMax, typeContrat) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, offre.getTitre());
            stmt.setString(2, offre.getDescription());
            stmt.setString(3, offre.getDomaine());
            stmt.setString(4, offre.getVille());
            stmt.setString(5, offre.getPays());
            stmt.setLong(6, offre.getRecruteur().getId());
            stmt.setDate(7, java.sql.Date.valueOf(offre.getDatePublication()));
            stmt.setDate(8, java.sql.Date.valueOf(offre.getDateExpiration()));

            // Gestion des salaires null
            if (offre.getSalaireMin() != null) {
                stmt.setDouble(9, offre.getSalaireMin());
            } else {
                stmt.setNull(9, Types.DOUBLE);
            }

            if (offre.getSalaireMax() != null) {
                stmt.setDouble(10, offre.getSalaireMax());
            } else {
                stmt.setNull(10, Types.DOUBLE);
            }

            stmt.setString(11, offre.getTypeContrat());


            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        offre.setId(rs.getLong(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Récupérer les offres par domaine
    public List<OffreEmploi> getOffresByDomaine(String domaine) {
        List<OffreEmploi> offres = new ArrayList<>();
        String sql = "SELECT * FROM OffreEmploi WHERE domaine = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, domaine);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                offres.add(mapResultSetToOffre(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offres;
    }

    private OffreEmploi mapResultSetToOffre(ResultSet rs) throws SQLException {
        OffreEmploi offre = new OffreEmploi();
        offre.setId(rs.getLong("id"));
        offre.setTitre(rs.getString("titre"));
        offre.setDescription(rs.getString("description"));
        offre.setDomaine(rs.getString("domaine"));
        offre.setVille(rs.getString("ville"));
        offre.setPays(rs.getString("pays"));

        // Initialiser seulement l'ID du recruteur
        Recruteur recruteur = new Recruteur();
        recruteur.setId(rs.getLong("recruteurId"));
        offre.setRecruteur(recruteur);

        offre.setDatePublication(rs.getDate("datePublication").toLocalDate());

        Date expiration = rs.getDate("dateExpiration");
        if (expiration != null) {
            offre.setDateExpiration(expiration.toLocalDate());
        }

        offre.setSalaireMin(rs.getDouble("salaireMin"));
        offre.setSalaireMax(rs.getDouble("salaireMax"));
        offre.setTypeContrat(rs.getString("typeContrat"));

        return offre;
    }

    // Méthodes supplémentaires
    public List<OffreEmploi> getOffresRecententes(Long limit) {
        List<OffreEmploi> offres = new ArrayList<>();
        String sql = "SELECT * FROM OffreEmploi ORDER BY datePublication DESC LIMIT ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                offres.add(mapResultSetToOffre(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offres;
    }

    public boolean deleteOffre(Long id) {
        String sql = "DELETE FROM OffreEmploi WHERE id = ?";
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