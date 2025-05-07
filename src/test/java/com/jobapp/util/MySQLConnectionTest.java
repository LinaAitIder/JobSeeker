package com.jobapp.util;


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
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class MySQLConnectionTest {

    @Autowired
    private DataSource dataSource;

    private MockedStatic<MySQLConnection> mysqlConnMock;

    @BeforeEach
    void setUp() {
        mysqlConnMock = Mockito.mockStatic(MySQLConnection.class);
        mysqlConnMock.when(MySQLConnection::getConnection)
                .thenAnswer(inv -> dataSource.getConnection());
    }

    @AfterEach
    void tearDown() {

        mysqlConnMock.close();
    }

    @Test
    void testGetConnection() {
        try {
            // 3. Obtenir une connexion (devrait venir de TestDB)
            Connection conn = MySQLConnection.getConnection();

            // 4. Vérifier que la connexion n'est pas null
            assertNotNull(conn, "La connexion ne devrait pas être null");

            // 5. Vérifier que la connexion n'est pas fermée
            assertFalse(conn.isClosed(), "La connexion devrait être ouverte");

            // 6. Fermer proprement la connexion
            conn.close();

            // 7. Vérifier qu'elle est bien fermée
            assertTrue(conn.isClosed(), "La connexion devrait être fermée après close()");

            System.out.println("Test de connexion réussi !");
        } catch (SQLException e) {
            fail("Échec de la connexion : " + e.getMessage());
        }
    }
}