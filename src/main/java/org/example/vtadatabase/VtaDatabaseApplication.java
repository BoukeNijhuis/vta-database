package org.example.vtadatabase;

import org.example.vtadatabase.infrastructure.persistence.DatabaseServer;

import java.sql.SQLException;

public class VtaDatabaseApplication {

    public static void main(String[] args) throws SQLException {
        new DatabaseServer().createDatabase();
    }
}