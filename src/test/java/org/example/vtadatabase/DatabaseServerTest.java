package org.example.vtadatabase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseServerTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = new DatabaseServer().createDatabase();
    }

    @Test
    public void testDatabaseConnection() {
        assertDoesNotThrow(() -> assertTrue(connection.isValid(5), "Database connection should be valid"));
    }

    @Test
    public void testRowsInTables() {
        assertRowsInTable("client", 2);
        assertRowsInTable("policy", 2);
        assertRowsInTable("client_policy", 2);
        assertRowsInTable("claim", 21);
    }

    private void assertRowsInTable(String tableName, int expectedRows) {
        assertDoesNotThrow(() -> {
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                        "select count(*) from demo." + tableName);
                // forward the pointer to the first row
                rs.next();
                final String message = String.format("Table %s should have %d rows", tableName, expectedRows);
                assertEquals(expectedRows, rs.getInt(1), message);
            }
        });
    }
}