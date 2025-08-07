package org.example.vtadatabase;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify database accessibility.
 */
public class DatabaseServerTest {

    private DatabaseServer databaseServer = new DatabaseServer();

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
        // Test that we can connect to the database
        assertDoesNotThrow(() -> {
            Connection connection = DriverManager.getConnection(databaseServer.getConnectionString(), "sa", "");
            assertTrue(connection.isValid(5), "Database connection should be valid");
        });
    }

    @Test
    public void testQueryOnUsersTable() {
        // Test that the schema and table exist
        assertDoesNotThrow(() -> {
            Connection connection = DriverManager.getConnection(databaseServer.getConnectionString(), "sa", "");

            try (Statement stmt = connection.createStatement()) {
                // Check if the users table exists
                ResultSet rs = stmt.executeQuery(
                        "SELECT COUNT(*) FROM USERS");
                // forward the pointer to the first row
                rs.next();
                assertEquals(2, rs.getInt(1), "Users table should have 2 rows");
            }
        });
    }
}