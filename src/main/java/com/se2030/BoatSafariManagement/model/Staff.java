package com.se2030.BoatSafariManagement.model;

public class Staff {
    private Integer staffId;
    private String laneNumber;
    private String city;
    private Boolean availability;
    private Double salary;
    private String role;
    private String firstName;
    private String lastName;
    private String email;

    // Constructors, Getters and Setters
    public Staff() {}

    public Staff(Integer staffId, String laneNumber, String city, Boolean availability,
                 Double salary, String role, String firstName, String lastName, String email) {
        this.staffId = staffId;
        this.laneNumber = laneNumber;
        this.city = city;
        this.availability = availability;
        this.salary = salary;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

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

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}