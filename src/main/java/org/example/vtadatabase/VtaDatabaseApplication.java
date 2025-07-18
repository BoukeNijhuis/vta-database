package org.example.vtadatabase;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VtaDatabaseApplication {

    public static void main(String[] args) throws SQLException {
        Server.createTcpServer("-ifNotExists").start();

        String workingDirectory = System.getProperty("user.dir");
        String scriptPath = workingDirectory + "/src/main/resources/h2init.sql";
        String connectionString = String.format("jdbc:h2:tcp://localhost/~/vtadb;INIT=RUNSCRIPT FROM '%s'", scriptPath);

        // create a connection == creating a database
        Connection conn = DriverManager.
                getConnection(connectionString, "sa", "");

    }
}