package org.example.vtadatabase.infrastructure.persistence;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseServer {
    private static final String connectionString = "jdbc:h2:tcp://localhost/~/vtadb";
    private static final String pathToSqlFile = "/src/main/resources/h2init.sql";

    public Connection createDatabase() throws SQLException {
        Server.createTcpServer("-ifNotExists").start();
        String workingDirectory = System.getProperty("user.dir");
        String scriptPath = workingDirectory + pathToSqlFile;
        String runScriptConnectionString = String.format("%s;INIT=RUNSCRIPT FROM '%s'", connectionString, scriptPath);
        return DriverManager.getConnection(runScriptConnectionString, "sa", "");
    }
}

