package com.jobapp.dao;

import com.jobapp.model.Entreprise;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class RecruteurDAOTest {

    @Autowired
    private DataSource dataSource;

    private RecruteurDAO recruteurDAO;
    private EntrepriseJdbcDAO entrepriseJdbcDAO;
    private MockedStatic<MySQLConnection> mysqlConnMock;

    private Recruteur testRecruteur;
    private Entreprise entrepriseTest;
    private String uniqueEmail;
    private String uniquePassword;

    @BeforeEach
    void setUp() throws SQLException {
        mysqlConnMock = Mockito.mockStatic(MySQLConnection.class);
        mysqlConnMock.when(MySQLConnection::getConnection)
                .thenAnswer(inv -> dataSource.getConnection());

        recruteurDAO      = new RecruteurDAO();
        entrepriseJdbcDAO = new EntrepriseJdbcDAO();

        // 1) Persister l'entreprise
        entrepriseTest = new Entreprise(
                "EntTest_" + UUID.randomUUID(),
                "Desc test",
                "Paris",
                Entreprise.TailleEntreprise.MOYENNE,
                "Informatique"
        );
        assertTrue(entrepriseJdbcDAO.save(entrepriseTest), "Entreprise non persistée");
        assertNotNull(entrepriseTest.getId(), "ID entreprise non généré");

        // 2) Préparer le recruteur
        uniqueEmail    = "test_" + UUID.randomUUID().toString().substring(0,8) + "@ex.com";
        uniquePassword = "pwd_"+ UUID.randomUUID().toString().substring(0,8);

        testRecruteur = new Recruteur();
        testRecruteur.setNom("Martin");
        testRecruteur.setPrenom("Sophie");
        testRecruteur.setEntreprise(entrepriseTest);
        testRecruteur.setEmail(uniqueEmail);
        testRecruteur.setPosition("RH");
        testRecruteur.setTelephone("0678901234");
        testRecruteur.setMotDePasse(uniquePassword);
        testRecruteur.setPhotoProfilPath("/path/to/photo.jpg");
    }

    @AfterEach
    void tearDown() throws SQLException {
        mysqlConnMock.close();
        try (Connection conn = dataSource.getConnection()) {
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
    void testRegisterRecruteur() {
        assertTrue(recruteurDAO.register(testRecruteur), "Register échoué");
        assertNotNull(testRecruteur.getId(), "ID recruteur non généré");
    }

    @Test
    void testLoginSuccess() {
        assertTrue(recruteurDAO.register(testRecruteur));
        Recruteur logged = recruteurDAO.login(uniqueEmail, uniquePassword);
        assertNotNull(logged, "Login échoué");
        assertEquals(entrepriseTest.getNom(), logged.getEntreprise().getNom());
    }

    @Test
    void testLoginFailure() {
        assertNull(recruteurDAO.login("bad@ex.com","nopass"), "Login invalide doit renvoyer null");
    }
}

