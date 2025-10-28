package org.example.vtadatabase.infrastructure.persistence;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseServer {
    private static final String connectionString = "jdbc:h2:tcp://localhost/~/vtadb";
    private static final String pathToSqlFile = "/src/main/resources/h2init.sql";
    private static volatile boolean initialized = false;

    public Connection createDatabase() throws SQLException {
        Server.createTcpServer("-ifNotExists").start();

        if (!initialized) {
            synchronized (DatabaseServer.class) {
                if (!initialized) {
                    String workingDirectory = System.getProperty("user.dir");
                    String scriptPath = workingDirectory + pathToSqlFile;
                    String runScriptConnectionString = String.format("%s;INIT=RUNSCRIPT FROM '%s'", connectionString, scriptPath);
                    try (Connection initConn = DriverManager.getConnection(runScriptConnectionString, "sa", "")) {
                        // Connection opened just to run the init script
                    }
                    initialized = true;
                }
            }
        }

        return DriverManager.getConnection(connectionString, "sa", "");
    }
}

