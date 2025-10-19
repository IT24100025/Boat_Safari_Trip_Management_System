package com.example.boatsafarisalarysystem.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=master;encrypt=false";
    private static final String USER = "sa";
    private static final String PASS = "123";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found. Add it via Maven.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed. Check credentials and SQL Server.");
            e.printStackTrace();
        }
        return null;
    }
}