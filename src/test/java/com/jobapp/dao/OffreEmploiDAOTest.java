package com.jobapp.dao;

import com.jobapp.model.Entreprise;
import com.jobapp.model.OffreEmploi;
import com.jobapp.model.Recruteur;
import com.jobapp.util.MySQLConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class OffreEmploiDAOTest {

    @Autowired
    private DataSource dataSource;

    private OffreEmploiDAO offreDAO;
    private RecruteurDAO recruteurDAO;
    private EntrepriseJdbcDAO entrepriseJdbcDAO;

    private OffreEmploi testOffre;
    private Recruteur recruteurTest;
    private Entreprise entrepriseTest;

    private MockedStatic<MySQLConnection> mysqlConnMock;
    private String uniqueEmail;
    private String uniquePassword;

    @BeforeEach
    void setUp() throws SQLException {
        // Mock statique
        mysqlConnMock = Mockito.mockStatic(MySQLConnection.class);
        mysqlConnMock.when(MySQLConnection::getConnection)
                .thenAnswer(inv -> dataSource.getConnection());

        // Instanciation des DAO
        offreDAO         = new OffreEmploiDAO();
        recruteurDAO     = new RecruteurDAO();
        entrepriseJdbcDAO = new EntrepriseJdbcDAO();

        // 1) Persister l'entreprise via le DAO
        entrepriseTest = new Entreprise(
                "TestEnt_" + UUID.randomUUID(),
                "Desc test",
                "Paris",
                Entreprise.TailleEntreprise.MOYENNE,
                "Informatique"
        );
        assertTrue(entrepriseJdbcDAO.save(entrepriseTest), "Entreprise persisting failed");
        assertNotNull(entrepriseTest.getId(), "Entreprise doit avoir un ID");

        // 2) Créer et persister le recruteur
        uniqueEmail    = "test-" + UUID.randomUUID() + "@mail.com";
        uniquePassword = "pwd-"  + UUID.randomUUID();

        recruteurTest = new Recruteur();
        recruteurTest.setNom("Test");
        recruteurTest.setPrenom("Recruteur");
        recruteurTest.setEntreprise(entrepriseTest);
        recruteurTest.setEmail(uniqueEmail);
        recruteurTest.setTelephone("0123456789");
        recruteurTest.setMotDePasse(uniquePassword);
        recruteurTest.setPosition("Manager");
        recruteurTest.setPhotoProfilPath("/path/to/photo.jpg");

        assertTrue(recruteurDAO.register(recruteurTest), "Recruteur persisting failed");
        Recruteur savedRec = recruteurDAO.login(uniqueEmail, uniquePassword);
        assertNotNull(savedRec, "Login after register failed");
        recruteurTest.setId(savedRec.getId());

        // 3) Préparer l'offre
        testOffre = new OffreEmploi();
        testOffre.setTitre("Dev Java");
        testOffre.setDescription("Spring Boot");
        testOffre.setDomaine("IT");
        testOffre.setVille("Lyon");
        testOffre.setPays("France");
        testOffre.setRecruteur(recruteurTest);
        testOffre.setSalaireMin(40000.0);
        testOffre.setSalaireMax(60000.0);
        testOffre.setTypeContrat("CDI");
        testOffre.setDatePublication(LocalDate.now());
        testOffre.setDateExpiration(LocalDate.now().plusMonths(1));
    }

    @AfterEach
    void tearDown() throws SQLException {
        mysqlConnMock.close();
        try (Connection conn = dataSource.getConnection()) {
            if (testOffre.getId() != null) {
                try (PreparedStatement st = conn.prepareStatement("DELETE FROM OffreEmploi WHERE id = ?")) {
                    st.setLong(1, testOffre.getId());
                    st.executeUpdate();
                }
            }
            try (PreparedStatement st = conn.prepareStatement("DELETE FROM Recruteur WHERE email = ?")) {
                st.setString(1, uniqueEmail);
                st.executeUpdate();
            }
            try (PreparedStatement st = conn.prepareStatement("DELETE FROM Entreprise WHERE id = ?")) {
                st.setLong(1, entrepriseTest.getId());
                st.executeUpdate();
            }
        }
    }

    @Test
    void testCreateOffre() {
        assertTrue(offreDAO.createOffre(testOffre), "Création d'offre échouée");
        assertNotNull(testOffre.getId(), "ID de l'offre non généré");
    }

    @Test
    void testGetOffresByDomaine() {
        offreDAO.createOffre(testOffre);
        List<OffreEmploi> list = offreDAO.getOffresByDomaine("IT");
        assertFalse(list.isEmpty(), "Liste par domaine vide");
    }

    @Test
    void testGetOffresRecententes() {
        offreDAO.createOffre(testOffre);
        List<OffreEmploi> rec = offreDAO.getOffresRecententes(30L);
        assertFalse(rec.isEmpty(), "Liste des récentes vide");
    }
}


