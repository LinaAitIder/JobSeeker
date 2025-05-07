package com.jobapp.dao;

import com.jobapp.model.Certification;
import com.jobapp.util.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;

public class CertificationDAO {
    public boolean save(Certification certification) {
        String sql = "INSERT INTO Certification (path, nom, candidat_id) VALUES (?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, certification.getPath());
            stmt.setString(2, certification.getNom());
            stmt.setLong(3, certification.getCandidat().getId());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0 && certification.getId() == null) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        certification.setId(generatedKeys.getLong(1));
                    }
                }
            }

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM Certification WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Certification> findByCandidatId(Long candidatId) {
        String sql = "SELECT * FROM Certification WHERE candidat_id = ?";
        List<Certification> certifications = new ArrayList<>();

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, candidatId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Certification cert = new Certification();
                cert.setId(rs.getLong("id"));
                cert.setPath(rs.getString("path"));
                cert.setNom(rs.getString("nom"));
                certifications.add(cert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return certifications;
    }
}
