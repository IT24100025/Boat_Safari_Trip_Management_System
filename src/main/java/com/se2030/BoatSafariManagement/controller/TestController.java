package com.se2030.BoatSafariManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-save")
    public String testSave() {
        String sql = "INSERT INTO [User] (Email,FirstName, LastName, Password, Role, CreatedDate) " +
                "VALUES ('abcd@gmail.com','Test', 'User', 'dummyhash', 'Admin', GETDATE())";
        try {
            int rows = jdbcTemplate.update(sql);
            return "Inserted " + rows + " row(s). Check your Admin table!";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}