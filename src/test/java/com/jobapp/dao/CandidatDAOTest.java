package com.jobapp.dao;

import com.jobapp.model.Candidat;
import com.jobapp.model.Certification;
import com.jobapp.util.MySQLConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class CandidatDAOTest {

    private CandidatDAO candidatDAO;
    private Candidat testCandidat;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        candidatDAO = new CandidatDAO();

        // Création d'un email unique pour chaque test
        String uniqueEmail = "test." + System.currentTimeMillis() + "@email.com";

        Certification cert1 = new Certification();
        cert1.setPath("/path/to/cert1.pdf");
        cert1.setNom("Certification 1");

        Certification cert2 = new Certification();
        cert2.setPath("/path/to/cert2.pdf");
        cert2.setNom("Certification 2");

        testCandidat = new Candidat(
                "Dupont",
                "Jean",
                "Paris",
                "France",
                "0123456789",
                uniqueEmail,
                "password123",
                "/path/to/cv.pdf",
                List.of(cert1, cert2),
                "/path/to/candidatprofil.jpeg"
        );
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Nettoyer la base de données après chaque test
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Candidat WHERE email LIKE 'test.%'")) {
            stmt.executeUpdate();
        }
    }

    @Test
    void testRegisterCandidat() {
        try (MockedStatic<MySQLConnection> mocked = Mockito.mockStatic(MySQLConnection.class)) {
            mocked.when(MySQLConnection::getConnection).thenReturn(dataSource.getConnection());

            boolean result = candidatDAO.register(testCandidat);
            assertTrue(result, "L'enregistrement devrait retourner true");

            // Vérifier que l'ID a bien été attribué
            assertNotNull(testCandidat.getId(), "L'ID du candidat devrait être généré");
        } catch (Exception e) {
            fail("L'enregistrement a échoué: " + e.getMessage());
        }
    }

    @Test
    void testLoginSuccess() {
        try (MockedStatic<MySQLConnection> mocked = Mockito.mockStatic(MySQLConnection.class)) {

            mocked.when(MySQLConnection::getConnection)
                    .thenAnswer(inv -> dataSource.getConnection());

            // Enregistrer le candidat
            boolean registered = candidatDAO.register(testCandidat);
            assertTrue(registered);

            // Tester le login
            Candidat loggedIn = candidatDAO.login(testCandidat.getEmail(), "password123");
            assertNotNull(loggedIn);
            assertEquals("Dupont", loggedIn.getNom());
            assertEquals(testCandidat.getEmail(), loggedIn.getEmail());
        } catch (Exception e) {
            fail("Le login a échoué: " + e.getMessage());
        }
    }

    @Test
    void testLoginFailure() {
        try (MockedStatic<MySQLConnection> mocked = Mockito.mockStatic(MySQLConnection.class)) {
            // Mock de la méthode statique MySQLConnection.getConnection()
            mocked.when(MySQLConnection::getConnection).thenReturn(dataSource.getConnection());

            Candidat loggedIn = candidatDAO.login("wrong@email.com", "wrongpassword");
            assertNull(loggedIn);
        } catch (Exception e) {
            fail("Le test a échoué: " + e.getMessage());
        }
    }
}