package com.jobapp.dao;

import com.jobapp.model.Entreprise;
import com.jobapp.model.Entreprise.TailleEntreprise;
import com.jobapp.util.MySQLConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class EntrepriseDAOTest {

    @Autowired
    private DataSource dataSource;

    private EntrepriseJdbcDAO entrepriseJdbcDAO;
    private MockedStatic<MySQLConnection> mysqlConnMock;

    @BeforeEach
    void setUp() throws SQLException {
        entrepriseJdbcDAO = new EntrepriseJdbcDAO();

        mysqlConnMock = mockStatic(MySQLConnection.class);
        mysqlConnMock.when(MySQLConnection::getConnection)
                .thenAnswer(invocation -> dataSource.getConnection());

        // Nettoyer la base car ces tests ne sont pas independants
        clearEntrepriseTable();
    }

    @AfterEach
    void tearDown() {
        if (mysqlConnMock != null) {
            mysqlConnMock.close();
        }
    }

    // ⚡ Ajout du nettoyage de la table entreprise
    private void clearEntrepriseTable() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM Entreprise");
        }
    }

    @Test
    void findByNom_ShouldReturnEntreprise_WhenExists() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO Entreprise (nom, description, location, taille, domaine) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "TechCorp");
                stmt.setString(2, "A tech company");
                stmt.setString(3, "San Francisco");
                stmt.setString(4, "MOYENNE");
                stmt.setString(5, "Software");
                stmt.executeUpdate();
            }

            Optional<Entreprise> result = entrepriseJdbcDAO.findByNom("TechCorp");

            assertTrue(result.isPresent());
            assertEquals("TechCorp", result.get().getNom());
            assertEquals(Entreprise.TailleEntreprise.MOYENNE, result.get().getTaille());
        }
    }

    @Test
    void findByNom_ShouldReturnEmpty_WhenNotExists() {
        Optional<Entreprise> result = entrepriseJdbcDAO.findByNom("Unknown");
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnList_WhenNotEmpty() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO Entreprise (nom, description, location, taille, domaine) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "TechCorp");
                stmt.setString(2, "A tech company");
                stmt.setString(3, "San Francisco");
                stmt.setString(4, "MOYENNE");
                stmt.setString(5, "Software");
                stmt.executeUpdate();
            }

            List<Entreprise> result = entrepriseJdbcDAO.findAll();
            assertFalse(result.isEmpty());
            assertEquals("TechCorp", result.get(0).getNom());
        }
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoEntreprises() {
        List<Entreprise> result = entrepriseJdbcDAO.findAll();
        assertTrue(result.isEmpty());
    }
}

