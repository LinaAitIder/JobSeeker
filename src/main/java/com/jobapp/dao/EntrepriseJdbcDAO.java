package com.jobapp.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.jobapp.model.Entreprise;
import com.jobapp.util.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntrepriseJdbcDAO {

    public boolean save(Entreprise entreprise) {
        String sql = "INSERT INTO Entreprise (nom, description, location, taille, domaine) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, entreprise.getNom());
            stmt.setString(2, entreprise.getDescription());
            stmt.setString(3, entreprise.getLocation());
            stmt.setString(4, entreprise.getTaille().name());
            stmt.setString(5, entreprise.getDomaine());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entreprise.setId(rs.getLong(1));
                    }
                }
            }

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Entreprise> findByNom(String nom) {
        String sql = "SELECT * FROM Entreprise WHERE nom = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Entreprise entreprise = new Entreprise();
                entreprise.setId(rs.getLong("id"));
                entreprise.setNom(rs.getString("nom"));
                entreprise.setDescription(rs.getString("description"));
                entreprise.setLocation(rs.getString("location"));
                entreprise.setTaille(Entreprise.TailleEntreprise.valueOf(rs.getString("taille")));
                entreprise.setDomaine(rs.getString("domaine"));
                return Optional.of(entreprise);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Entreprise> findAll() {
        String sql = "SELECT * FROM Entreprise";
        List<Entreprise> entreprises = new ArrayList<>();

        try (Connection conn = MySQLConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Entreprise entreprise = new Entreprise();
                entreprise.setId(rs.getLong("id"));
                entreprise.setNom(rs.getString("nom"));
                entreprise.setDescription(rs.getString("description"));
                entreprise.setLocation(rs.getString("location"));
                entreprise.setTaille(Entreprise.TailleEntreprise.valueOf(rs.getString("taille")));
                entreprise.setDomaine(rs.getString("domaine"));
                entreprises.add(entreprise);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entreprises;
    }
}
