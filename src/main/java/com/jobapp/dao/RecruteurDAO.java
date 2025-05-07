package com.jobapp.dao;

import com.jobapp.model.Entreprise;
import com.jobapp.model.Recruteur;
import com.jobapp.util.MySQLConnection;

import java.sql.*;

public class RecruteurDAO {

    // ENREGISTRE UN NOUVEAU RECRUTEUR
    public boolean register(Recruteur recruteur) {
        String sql = "INSERT INTO Recruteur (nom, prenom, entreprise_id, email, position, telephone, mot_de_passe) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, recruteur.getNom());
            stmt.setString(2, recruteur.getPrenom());
            stmt.setLong(3, recruteur.getEntreprise().getId());
            stmt.setString(4, recruteur.getEmail());
            stmt.setString(5, recruteur.getPosition());
            stmt.setString(6, recruteur.getTelephone());
            stmt.setString(7, recruteur.getMotDePasse());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        recruteur.setId(rs.getLong(1));
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Recruteur login(String email, String motDePasse) {
        String sql = "SELECT r.*, e.nom as entreprise_nom FROM Recruteur r " +
                "LEFT JOIN Entreprise e ON r.entreprise_id = e.id " +
                "WHERE r.email = ? AND r.mot_de_passe = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Recruteur recruteur = new Recruteur();
                recruteur.setId(rs.getLong("id"));
                recruteur.setNom(rs.getString("nom"));
                recruteur.setPrenom(rs.getString("prenom"));
                recruteur.setEmail(rs.getString("email"));
                recruteur.setPosition(rs.getString("position"));
                recruteur.setTelephone(rs.getString("telephone"));
                recruteur.setMotDePasse(rs.getString("mot_de_passe"));
                recruteur.setPhotoProfilPath(rs.getString("photo_profil_path"));

                // Création de l'objet Entreprise minimal
                Entreprise entreprise = new Entreprise();
                entreprise.setId(rs.getLong("entreprise_id"));
                entreprise.setNom(rs.getString("entreprise_nom"));
                recruteur.setEntreprise(entreprise);

                return recruteur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteRecruteur(Long id) {
        String sql = "DELETE FROM Recruteur WHERE id = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteRecruteurByEmail(String email) {
        String sql = "DELETE FROM Recruteur WHERE email = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
