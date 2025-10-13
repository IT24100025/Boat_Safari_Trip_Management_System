package com.example.se2030.BoatSafariManagement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private LocalDateTime createdDate;
}