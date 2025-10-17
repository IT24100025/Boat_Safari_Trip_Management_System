package com.se2030.BoatSafariManagement.model;

public class Boat {
    private int boatId;
    private String boatName;
    private String type;
    private int capacity;
    private String availability;
    private String status;
    private Integer boatOwnerId;

    // Getters & Setters
    public int getBoatId() {
        return boatId;
    }
    public void setBoatId(int boatId) {
        this.boatId = boatId;
    }

    public String getBoatName() {
        return boatName;
    }
    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBoatOwnerId() {
        return boatOwnerId;
    }
    public void setBoatOwnerId(Integer boatOwnerId) {
        this.boatOwnerId = boatOwnerId;
    }
}

