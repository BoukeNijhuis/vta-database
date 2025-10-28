package org.example.vtadatabase.infrastructure.persistence;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages H2 database server lifecycle and connection creation.
 * Uses a singleton pattern to ensure the database is initialized only once.
 */
public class DatabaseServer {
    private static final String CONNECTION_STRING = "jdbc:h2:tcp://localhost/~/vtadb";
    private static final String INIT_SCRIPT_PATH = "/src/main/resources/h2init.sql";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "";
    private static final String TCP_SERVER_ARGS = "-ifNotExists";

    private static volatile boolean initialized = false;

    /**
     * Creates a connection to the H2 database.
     * Initializes the database with the init script on first call.
     *
     * @return a database connection
     * @throws SQLException if a database access error occurs
     */
    public Connection createDatabase() throws SQLException {
        startServerIfNeeded();
        initializeDatabaseIfNeeded();
        return createConnection();
    }

    private void startServerIfNeeded() throws SQLException {
        Server.createTcpServer(TCP_SERVER_ARGS).start();
    }

    private void initializeDatabaseIfNeeded() throws SQLException {
        if (!initialized) {
            synchronized (DatabaseServer.class) {
                if (!initialized) {
                    runInitializationScript();
                    initialized = true;
                }
            }
        }
    }

    private void runInitializationScript() throws SQLException {
        String scriptPath = getAbsoluteScriptPath();
        String connectionStringWithInit = buildConnectionStringWithInit(scriptPath);

        try (Connection _ = DriverManager.getConnection(connectionStringWithInit, DB_USERNAME, DB_PASSWORD)) {
            // Connection opened just to run the init script, then auto-closed
        }
    }

    private String getAbsoluteScriptPath() {
        String workingDirectory = System.getProperty("user.dir");
        return workingDirectory + INIT_SCRIPT_PATH;
    }

    private String buildConnectionStringWithInit(String scriptPath) {
        return String.format("%s;INIT=RUNSCRIPT FROM '%s'", CONNECTION_STRING, scriptPath);
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
    }
}

