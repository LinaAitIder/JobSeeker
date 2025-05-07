package com.jobapp.dao;

import com.jobapp.model.Candidat;
import com.jobapp.model.Certification;
import com.jobapp.util.MySQLConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CertificationDAOTest {

    private CertificationDAO certificationDAO;
    private MockedStatic<MySQLConnection> mysqlConnMock;
    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;

    @BeforeEach
    void setUp() throws SQLException {
        certificationDAO = new CertificationDAO();

        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockConn.prepareStatement(anyString(), anyInt())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockStmt.getGeneratedKeys()).thenReturn(mockRs); // <-- CECI est nécessaire

        mysqlConnMock = Mockito.mockStatic(MySQLConnection.class);
        mysqlConnMock.when(MySQLConnection::getConnection)
                .thenReturn(mockConn);
    }



    @AfterEach
    void tearDown() {
        if (mysqlConnMock != null) {
            mysqlConnMock.close();
        }
    }

    @Test
    void save_ShouldReturnTrue_WhenInsertSucceeds() throws SQLException {
        Certification cert = new Certification();
        cert.setNom("Java Certification");
        cert.setPath("/certifs/java.pdf");

        // Candidat fictif
        Candidat candidat = new Candidat();
        candidat.setId(1L);
        cert.setCandidat(candidat);

        when(mockStmt.executeUpdate()).thenReturn(1);

        boolean result = certificationDAO.save(cert);

        assertTrue(result, "La sauvegarde de la certification doit réussir");
    }

    @Test
    void findByCandidatId_ShouldReturnCertifications_WhenExist() throws SQLException {
        when(mockRs.next()).thenReturn(true).thenReturn(false);
        when(mockRs.getLong("id")).thenReturn(1L);
        when(mockRs.getString("nom")).thenReturn("Java Certification");
        when(mockRs.getString("path")).thenReturn("/certifs/java.pdf");

        List<Certification> certifications = certificationDAO.findByCandidatId(1L);

        assertNotNull(certifications);
        assertEquals(1, certifications.size());
        assertEquals("Java Certification", certifications.get(0).getNom());
    }

    @Test
    void delete_ShouldReturnTrue_WhenDeleteSucceeds() throws SQLException {
        when(mockStmt.executeUpdate()).thenReturn(1);

        boolean result = certificationDAO.delete(1L);

        assertTrue(result, "La suppression de la certification doit réussir");
    }
}
