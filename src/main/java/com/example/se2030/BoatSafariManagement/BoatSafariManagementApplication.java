package com.example.se2030.BoatSafariManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoatSafariManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoatSafariManagementApplication.class, args);
        
        // Display startup message
        displayStartupMessage();
    }

    private static void displayStartupMessage() {
        System.out.println("\n\n");
        System.out.println("==================================================");
        System.out.println("BOAT SAFARI MANAGEMENT SYSTEM (JDBC Version)");
        System.out.println("==================================================");
        System.out.println("Application Started Successfully!");
        System.out.println("Open your browser and go to: http://localhost:8080");
        System.out.println("Database: Using JDBC with SQL Server");
        System.out.println("Server: Running on port 8080");
        System.out.println("==================================================");
        System.out.println("\n");
    }
}