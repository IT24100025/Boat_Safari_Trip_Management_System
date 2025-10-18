package com.se2030.BoatSafariManagement.model;

import java.math.BigDecimal;

public class Staff {
    private int staffId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String position;
    private BigDecimal salary;
    private String laneNumber;
    private String city;
    private Boolean availability;
    private String role;

    // Getters and Setters
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public String getLaneNumber() { return laneNumber; }
    public void setLaneNumber(String laneNumber) { this.laneNumber = laneNumber; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Boolean getAvailability() { return availability; }
    public void setAvailability(Boolean availability) { this.availability = availability; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}