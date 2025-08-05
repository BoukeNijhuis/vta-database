package org.example.vtadatabase;

import org.h2.tools.Server;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseServer {

    private Server server;

    private static final String connectionString = "jdbc:h2:tcp://localhost/~/vtadb";
    private static final String pathToSqlFile = "/src/main/resources/h2init.sql";

    public void createDatabase() throws SQLException {
        server = startServer();

        String workingDirectory = System.getProperty("user.dir");
        String scriptPath = workingDirectory + pathToSqlFile;
        String connectionCreationString = String.format("%sINIT=RUNSCRIPT FROM '%s'", connectionString, scriptPath);

        // create a connection == creating a database
        DriverManager.getConnection(connectionCreationString, "sa", "");
    }

    private Server startServer() throws SQLException {
        return Server.createTcpServer("-ifNotExists").start();
    }

    private void stopServer() {
        server.stop();
    }

    public String getConnectionString() {
        return connectionString;
    }
}
