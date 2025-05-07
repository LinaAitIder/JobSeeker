package com.jobapp.dao;

import com.jobapp.model.*;
import com.jobapp.repository.EntrepriseDAO;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class CandidatureDAOTest {

    @Autowired
    private DataSource dataSource;

    private CandidatureDAO candidatureDAO;
    private CandidatDAO candidatDAO;
    private OffreEmploiDAO offreEmploiDAO;
    private RecruteurDAO recruteurDAO;
    private EntrepriseJdbcDAO entrepriseJdbcDAO;

    private MockedStatic<MySQLConnection> mysqlConnMock;

    private Candidat candidatTest;
    private OffreEmploi offreTest;
    private Recruteur recruteurTest;
    private Entreprise entrepriseTest;

    private String generateUniqueEmail() {
        return "test_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    private String generateUniquePassword() {
        return "password" + UUID.randomUUID().toString().substring(0, 8);
    }

    @BeforeEach
    void setUp() throws SQLException {
        candidatureDAO = new CandidatureDAO();
        offreEmploiDAO = new OffreEmploiDAO();
        recruteurDAO = new RecruteurDAO();
        candidatDAO = new CandidatDAO();
        entrepriseJdbcDAO = new EntrepriseJdbcDAO();

        mysqlConnMock = mockStatic(MySQLConnection.class);
        mysqlConnMock.when(MySQLConnection::getConnection)
                .thenAnswer(invocation -> dataSource.getConnection());

        // 1. Sauvegarde entreprise avec ID généré
        entrepriseTest = new Entreprise(
                "TestCorp_" + UUID.randomUUID(),
                "Entreprise de test",
                "Paris",
                Entreprise.TailleEntreprise.MOYENNE,
                "Informatique"
        );
        assertTrue(entrepriseJdbcDAO.save(entrepriseTest), "L'entreprise doit être sauvegardée");
        assertNotNull(entrepriseTest.getId(), "L'entreprise doit avoir un ID");

        // 2. Crée le recruteur lié à l'entreprise
        recruteurTest = new Recruteur();
        recruteurTest.setNom("Recruteur");
        recruteurTest.setPrenom("Test");
        recruteurTest.setEntreprise(entrepriseTest);
        recruteurTest.setEmail(generateUniqueEmail());
        recruteurTest.setMotDePasse(generateUniquePassword());
        recruteurTest.setTelephone("0123456789");
        recruteurTest.setPosition("HR Manager");
        assertTrue(recruteurDAO.register(recruteurTest), "Le recruteur doit être sauvegardé");

        recruteurTest = recruteurDAO.login(recruteurTest.getEmail(), recruteurTest.getMotDePasse());
        assertNotNull(recruteurTest.getId(), "Le recruteur doit avoir un ID");

        // 3. Crée le candidat
        candidatTest = new Candidat(
                "Dupont", "Jean", "Paris", "France", "0123456789",
                generateUniqueEmail(), generateUniquePassword(),
                "/path/to/cv.pdf", List.of(), "/path/to/photo.jpg"
        );
        assertTrue(candidatDAO.register(candidatTest), "Le candidat doit être sauvegardé");
        candidatTest = candidatDAO.login(candidatTest.getEmail(), candidatTest.getMotDePasse());
        assertNotNull(candidatTest.getId(), "Le candidat doit avoir un ID");

        // 4. Crée l'offre
        offreTest = new OffreEmploi();
        offreTest.setTitre("Dev Java");
        offreTest.setDescription("Spring Boot Developer");
        offreTest.setDomaine("IT");
        offreTest.setVille("Paris");
        offreTest.setPays("France");
        offreTest.setRecruteur(recruteurTest);
        offreTest.setDatePublication(LocalDate.now());
        offreTest.setDateExpiration(LocalDate.now().plusMonths(1));
        assertTrue(offreEmploiDAO.createOffre(offreTest), "L'offre doit être sauvegardée");
    }

    @AfterEach
    void tearDown() throws SQLException {
        mysqlConnMock.close();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement p1 = conn.prepareStatement("DELETE FROM candidature WHERE offre_id = ?");
             PreparedStatement p2 = conn.prepareStatement("DELETE FROM offre_emploi WHERE recruteur_id = ?");
             PreparedStatement p3 = conn.prepareStatement("DELETE FROM candidat WHERE email LIKE 'test_%'");
             PreparedStatement p4 = conn.prepareStatement("DELETE FROM recruteur WHERE email LIKE 'test_%'");
             PreparedStatement p5 = conn.prepareStatement("DELETE FROM Entreprise WHERE id = ?")) {

            p1.setLong(1, offreTest.getId()); p1.executeUpdate();
            p2.setLong(1, recruteurTest.getId()); p2.executeUpdate();
            p3.executeUpdate();
            p4.executeUpdate();
            p5.setLong(1, entrepriseTest.getId()); p5.executeUpdate();  // suppression après
        }
    }

    @Test
    void testCreateCandidature() {
        Candidature c = new Candidature();
        c.setCandidat(candidatTest);
        c.setStatut(Candidature.Statut.EN_ATTENTE);
        c.setOffre(offreTest);
        c.setLettreMotivationPath("/path/let.pdf");
        c.setMessageRecruteur("Immédiat");
        c.setDatePostulation(LocalDateTime.now());

        assertTrue(candidatureDAO.createCandidature(c), "La création de la candidature doit réussir");
        assertTrue(c.getId() > 0, "ID de candidature doit être généré");
    }

    @Test
    void testGetCandidaturesByOffre() {
        Candidature c = new Candidature();
        c.setCandidat(candidatTest);
        c.setStatut(Candidature.Statut.EN_ATTENTE);
        c.setOffre(offreTest);
        c.setLettreMotivationPath("/path/let2.pdf");
        c.setMessageRecruteur("Msg");
        c.setDatePostulation(LocalDateTime.now());

        assertTrue(candidatureDAO.createCandidature(c), "La création de la candidature doit réussir");

        List<Candidature> list = candidatureDAO.getCandidaturesByOffre(offreTest.getId());
        assertFalse(list.isEmpty(), "Doit y avoir au moins une candidature");
        assertEquals(1, list.size(), "Exactement une candidature attendue");
        assertEquals(c.getId(), list.get(0).getId(), "Les IDs doivent correspondre");
    }

    @Test
    void testUpdateStatut() {
        Candidature c = new Candidature();
        c.setCandidat(candidatTest);
        c.setStatut(Candidature.Statut.EN_ATTENTE);
        c.setOffre(offreTest);
        c.setLettreMotivationPath("/path/lettreMotivation3.pdf");
        c.setMessageRecruteur("Message pour recruteur");
        c.setDatePostulation(LocalDateTime.now());
        assertTrue(candidatureDAO.createCandidature(c), "La création de la candidature doit réussir");

        boolean updated = candidatureDAO.updateStatut(c.getId(), Candidature.Statut.ACCEPTEE);
        assertTrue(updated, "Mise à jour du statut doit réussir");

        List<Candidature> candidatures = candidatureDAO.getCandidaturesByOffre(offreTest.getId());
        assertEquals(Candidature.Statut.ACCEPTEE, candidatures.get(0).getStatut(), "Le statut doit être 'ACCEPTEE'");
    }
}




