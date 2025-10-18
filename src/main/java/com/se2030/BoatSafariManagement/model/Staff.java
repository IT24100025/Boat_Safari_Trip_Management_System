package com.se2030.BoatSafariManagement.model;

public class Staff {
    private int staffId;
    private String name;      // e.g. First + Last from User
    private String role;
    private String email;// New field

    // getters and setters
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

}

