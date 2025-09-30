package com.boatsafarimanagement.model;

public class Staff {
    private int staffId;
    private String name;      // e.g. First + Last from User
    private String role;// New field

    // getters and setters
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

