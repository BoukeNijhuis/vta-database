package org.example.vtadatabase;

import java.sql.SQLException;

public class VtaDatabaseApplication {

    public static void main(String[] args) throws SQLException {
        new DatabaseServer().createDatabase();
    }
}