package org.example.vtadatabase;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseServerTest {

    private final DatabaseServer databaseServer = new DatabaseServer();

    @BeforeEach
    public void setUp() throws SQLException {
        databaseServer.createDatabase();
    }

    @AfterEach
    public void tearDown() {
        // Close connection and stop server
    }

    @Test
    public void testDatabaseConnection() {
        assertDoesNotThrow(() -> {
            Connection connection = DriverManager.getConnection(databaseServer.getConnectionString(), "sa", "");
            assertTrue(connection.isValid(5), "Database connection should be valid");
        });
    }

    @Test
    public void testQueryOnUsersTable() {
        assertDoesNotThrow(() -> {
            Connection connection = DriverManager.getConnection(databaseServer.getConnectionString(), "sa", "");

            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                        "SELECT count(*) FROM clients");
                // forward the pointer to the first row
                rs.next();
                assertEquals(2, rs.getInt(1), "Users table should have 2 rows");
            }
        });
    }
}