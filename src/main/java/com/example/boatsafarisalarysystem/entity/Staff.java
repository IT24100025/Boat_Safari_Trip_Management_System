package com.example.boatsafarisalarysystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Staff")
public class Staff {
    @Id
    @Column(name = "StaffId")
    private Integer staffId;

    @Column(name = "LaneNumber")
    private String laneNumber;

    @Column(name = "City")
    private String city;

    @Column(name = "Availability")
    private Boolean availability;

    @Column(name = "Salary")
    private Double salary;

    @Column(name = "Name")
    private String name;

    @Column(name = "Role")
    private String role;

    // Getters and Setters
    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public String getLaneNumber() { return laneNumber; }
    public void setLaneNumber(String laneNumber) { this.laneNumber = laneNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Boolean getAvailability() { return availability; }
    public void setAvailability(Boolean availability) { this.availability = availability; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}