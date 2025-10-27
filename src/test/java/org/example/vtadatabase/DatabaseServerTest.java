package org.example.vtadatabase;

import io.quarkus.test.junit.QuarkusTest;
import org.example.vtadatabase.infrastructure.persistence.DatabaseServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
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
        assertRowsInTable("client", 11); // updated after adding 11th client
        assertRowsInTable("policy", 3);
        assertRowsInTable("client_policy", 13); // Updated row count after giving three clients a second policy
        assertRowsInTable("claim", 24);
    }

    private void assertRowsInTable(String tableName, int expectedRows) {
        assertDoesNotThrow(() -> {
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                        "select count(*) from demo." + tableName);
                // forward the pointer to the first row
                rs.next();
                final String message = String.format("Table %s should have %d rows, but found %d rows", tableName, expectedRows, rs.getInt(1));
                assertEquals(expectedRows, rs.getInt(1), message);
            }
        });
    }

    @SuppressWarnings("SqlResolve")
    @Test
    public void testNoDuplicateClientPolicies() {
        assertDoesNotThrow(() -> {
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(
                     "select client_id, policy_id, count(*) as cnt " +
                     "from demo.client_policy " +
                     "group by client_id, policy_id " +
                     "having count(*) > 1")) {
                assertFalse(rs.next(), "Expected no duplicate client-policy assignments, but found duplicates");
            }
        });
    }
}